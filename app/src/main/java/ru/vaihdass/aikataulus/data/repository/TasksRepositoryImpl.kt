package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.base.Constants.EMPTY_VALUE_STRING
import ru.vaihdass.aikataulus.base.Constants.GOOGLE_TASKS_DATE_PATTERN
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_TASKS_LIST_ID
import ru.vaihdass.aikataulus.data.auth.TokenStorage
import ru.vaihdass.aikataulus.data.local.db.dao.TaskDao
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import ru.vaihdass.aikataulus.data.mapper.TaskListMapper
import ru.vaihdass.aikataulus.data.mapper.TaskMapper
import ru.vaihdass.aikataulus.data.remote.api.TasksApi
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.TaskList
import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import ru.vaihdass.aikataulus.utils.ResManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val tasksApi: TasksApi,
    private val taskListMapper: TaskListMapper,
    private val taskMapper: TaskMapper,
    private val preferencesManager: SharedPreferencesManager,
    private val tokenStorage: TokenStorage,
    private val resManager: ResManager,
    private val taskDao: TaskDao,
) : TasksRepository {
    private val taskListId =
        preferencesManager.getString(PREF_GOOGLE_TASKS_LIST_ID, EMPTY_VALUE_STRING)
            ?: EMPTY_VALUE_STRING

    override suspend fun getTaskLists(): List<TaskListDomainModel> {
        val taskListsResponse = tasksApi.getAllTaskLists()

        taskListsResponse.items?.let {
            return taskListMapper.mapNotNull(it)
        }

        throw RuntimeException(taskListsResponse.error?.message)
    }

    override suspend fun createTaskList(name: String): TaskListDomainModel {
        val taskList = tasksApi.insertTaskList(TaskList(name = name))

        taskList?.let {
            return taskListMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_create_task_list))
    }

    override suspend fun deleteTaskList(id: String) {
        tasksApi.removeTaskList(id)
    }

    override suspend fun getTodayTasks(): List<TaskDomainModel> {
        val (startOfDayMs, endOfDayMs) = getStartAndEndOfDayTimestampMs()

        return try {
            presyncUnsaved()

            val (dayStart, _) = getStartAndEndOfDayApiFormatted()

            val tasks =
                tasksApi.getTasks(taskListId = taskListId, from = dayStart, to = null)?.items
                    ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_tasks_for_today))

            taskDao.deleteAllFromTo(startOfDayMs, endOfDayMs)
            taskDao.insertOrUpdateAll(taskMapper.mapToDbEntities(tasks))

            taskMapper.fromDbEntities(taskDao.getAllFromTo(from = startOfDayMs, to = endOfDayMs))
        } catch (e: Exception) {
            taskMapper.fromDbEntities(taskDao.getAllFromTo(from = startOfDayMs, to = endOfDayMs))
        }
    }

    override suspend fun getTasksGroupBySubject(): Map<String, List<TaskDomainModel>> {
        val tasks = getAllTasks()
        return tasks.groupBy { task -> task.subject }
    }

    override suspend fun getAllTasks(): List<TaskDomainModel> {
        return try {
            presyncUnsaved()

            val tasks = tasksApi.getTasks(taskListId = taskListId, from = null, to = null)?.items
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_all_tasks))

            taskDao.deleteAll()
            taskDao.insertOrUpdateAll(taskMapper.mapToDbEntities(tasks))

            taskMapper.fromDbEntities(taskDao.getAll())
        } catch (e: Exception) {
            taskMapper.fromDbEntities(taskDao.getAll())
        }
    }

    override suspend fun getTask(task: TaskDomainModel): TaskDomainModel {
        return try {
            val tasks = tasksApi.getTask(taskListId = taskListId, taskId = task.id)
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_task))

            taskMapper.mapNotNull(tasks)
        } catch (e: Exception) {
            taskDao.getById(task.id)?.let { taskMapper.mapNotNull(it) }
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_task))
        }
    }

    override suspend fun editTask(task: TaskDomainModel): TaskDomainModel {
        try {
            presyncUnsaved()

            val taskResponse = tasksApi.patchTask(taskListId, task.id, taskMapper.mapNotNull(task))
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_edit_task))

            val taskEntity = taskMapper.mapToDbEntityNotNull(task)
            taskDao.update(taskEntity)

            return taskMapper.mapNotNull(taskResponse)
        } catch (e: IOException) {
            val taskEntity = taskMapper.mapToDbEntityNotNull(task, saved = false)
            taskDao.update(taskEntity)

            return taskMapper.mapNotNull(taskEntity)
        } catch (e: Exception) {
            taskDao.delete(task.id)
            throw RuntimeException(resManager.getString(R.string.unexpected_exception_due_editing_task))
        }
    }

    override suspend fun deleteTask(taskId: String) {
        try {
            presyncUnsaved()

            tasksApi.removeTask(taskListId, taskId)
            taskDao.delete(taskId)
        } catch (e: IOException) {
            val task = taskDao.getById(taskId)
            task?.let {
                task.deleted = true
                task.saved = false
                taskDao.update(task)
            }
        } catch (e: Exception) {
            taskDao.delete(taskId)
        }
    }

    override suspend fun removeDoneTasks() {
        try {
            presyncUnsaved()

            tasksApi.removeDoneFromTaskList(taskListId = taskListId)

            taskDao.deleteAllDone()
        } catch (ignored: Exception) {}
    }

    private suspend fun presyncUnsaved() {
        syncRemovedTasks()
        syncEditedTasks()
        syncCreatedTasks()
    }

    override suspend fun syncRemovedTasks() {
        val removed = taskMapper.fromDbEntities(taskDao.getAllDeletedUnsaved())

        removed.forEach {
            runCatching {
                tasksApi.removeTask(taskListId = taskListId, taskId = it.id)
            }
        }

        taskDao.deleteAllDeleted()
    }

    override suspend fun syncEditedTasks() {
        val unsaved = taskMapper.fromDbEntities(taskDao.getAllUnsaved())

        unsaved.map { taskMapper.mapNotNull(it) }
            .forEach { tasksApi.patchTask(taskListId = taskListId, taskId = it.id, task = it) }

        taskDao.markAllAsSaved()
    }

    override suspend fun syncCreatedTasks() {
        val created = taskMapper.fromDbEntities(taskDao.getAllCreatedUnsaved())

        created.map { taskMapper.mapNotNull(it) }
            .forEach {
                val oldId = it.id
                it.id = ""
                val createdTask = tasksApi.insertTask(taskListId = taskListId, task = it)
                taskDao.updateId(oldId, createdTask.id)
            }

        taskDao.markAllCreatedOfflineAsSaved()
    }

    override suspend fun createTask(task: TaskDomainModel): TaskDomainModel {
        try {
            presyncUnsaved()

            task.id = ""
            val createdTask = tasksApi.insertTask(taskListId, taskMapper.mapNotNull(task))

            taskDao.add(taskMapper.mapToDbEntityNotNull(createdTask))
            return taskMapper.mapNotNull(createdTask)
        } catch (e: RuntimeException) {
            task.id = UUID.randomUUID().toString()
            taskDao.add(
                taskMapper.mapToDbEntityNotNull(
                    task = task,
                    saved = false,
                    createdOffline = true
                )
            )

            return task
        }
    }

    override fun isAuthorized(): Boolean {
        val accessToken = tokenStorage.accessToken
        val refreshToken = tokenStorage.refreshToken

        return accessToken != null && refreshToken != null && accessToken != EMPTY_VALUE_STRING && refreshToken != EMPTY_VALUE_STRING
    }

    private fun getStartAndEndOfDayApiFormatted(): Pair<String, String> {
        val (startOfDay, endOfDay) = getStartAndEndOfDay()

        val dateFormatter = SimpleDateFormat(GOOGLE_TASKS_DATE_PATTERN, Locale.getDefault())
        dateFormatter.timeZone = TimeZone.getTimeZone("UTC")

        val startOfDayFormatted = dateFormatter.format(Date(startOfDay.toEpochSecond() * 1_000L))
        val endOfDayFormatted = dateFormatter.format(Date(endOfDay.toEpochSecond() * 1_000L))

        return startOfDayFormatted to endOfDayFormatted
    }

    private fun getStartAndEndOfDay(): Pair<ZonedDateTime, ZonedDateTime> {
        val zoneId = ZoneId.systemDefault()

        val now = ZonedDateTime.now(zoneId)
        val startOfDay = now.toLocalDate().atStartOfDay(zoneId)
        val endOfDay = now.toLocalDate().atTime(23, 59, 59, 999_999_999).atZone(zoneId)

        return startOfDay.withZoneSameInstant(ZoneId.of("UTC")) to endOfDay.withZoneSameInstant(
            ZoneId.of("UTC")
        )
    }

    private fun getStartAndEndOfDayTimestampMs(): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()

        val startOfDay = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay =
            LocalDate.now().atTime(LocalTime.MAX).atZone(zoneId).toInstant().toEpochMilli()

        return Pair(startOfDay, endOfDay)
    }
}