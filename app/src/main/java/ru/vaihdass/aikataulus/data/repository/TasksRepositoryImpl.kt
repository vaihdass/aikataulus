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
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone
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
        val (dayStart, _) = getStartAndEndOfDayFormatted()

        val tasks = tasksApi.getTasks(
            taskListId = taskListId, from = dayStart, to = null
        )

        tasks?.items?.let {
            taskMapper.mapToDbEntities(it)?.let { tasksDb ->
                taskDao.insertOrUpdateAll(tasks = tasksDb)

                val (startOfDay, endOfDay) = getStartAndEndOfDay()
                return taskMapper.fromDbEntities(
                    taskDao.getAllFromTo(
                        from = startOfDay.toEpochSecond() * 1_000L,
                        to = endOfDay.toEpochSecond() * 1_000L,
                    )
                )
            }
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_get_tasks_for_today))
    }

    private fun getStartAndEndOfDayFormatted(): Pair<String, String> {
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

    override suspend fun getTasksGroupBySubject(): Map<String, List<TaskDomainModel>> {
        val tasks = getAllTasks()
        return tasks.groupBy { task -> task.subject }
    }

    override suspend fun getAllTasks(): List<TaskDomainModel> {
        val tasks = tasksApi.getTasks(taskListId = taskListId, from = null, to = null)

        tasks?.items?.let {
            return taskMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_get_all_tasks))
    }

    override suspend fun getTask(task: TaskDomainModel): TaskDomainModel {
        val tasks = tasksApi.getTask(taskListId = taskListId, taskId = task.id)

        tasks?.let {
            return taskMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_get_task))
    }

    override suspend fun editTask(task: TaskDomainModel): TaskDomainModel {
        try {
            val taskResponse = tasksApi.patchTask(taskListId, task.id, taskMapper.mapNotNull(task))
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_edit_task))

            val taskEntity = taskMapper.mapToDbEntityNotNull(task)
            taskDao.update(taskEntity)

            return taskMapper.mapNotNull(taskResponse)
        } catch (e: RuntimeException) {
            val taskEntity = taskMapper.mapToDbEntityNotNull(task, saved = false)
            taskDao.update(taskEntity)

            throw RuntimeException(e)
        }
    }

    override suspend fun deleteTask(taskId: String) {
        runCatching {
            tasksApi.removeTask(taskListId, taskId)
        }
    }

    override suspend fun removeDoneTasks() {
        runCatching {
            tasksApi.removeDoneFromTaskList(taskListId = taskListId)
        }
    }

    override suspend fun createTask(task: TaskDomainModel): TaskDomainModel {
        return taskMapper.mapNotNull(tasksApi.insertTask(taskListId, taskMapper.mapNotNull(task)))
    }

    override fun isAuthorized(): Boolean {
        val accessToken = tokenStorage.accessToken
        val refreshToken = tokenStorage.refreshToken

        return accessToken != null && refreshToken != null && accessToken != EMPTY_VALUE_STRING && refreshToken != EMPTY_VALUE_STRING
    }
}