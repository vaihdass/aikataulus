package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_ACCESS_TOKEN_KEY
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_REFRESH_TOKEN_KEY
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import ru.vaihdass.aikataulus.data.mapper.TaskListMapper
import ru.vaihdass.aikataulus.data.remote.api.TasksApi
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.TaskList
import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val tasksApi: TasksApi,
    private val mapper: TaskListMapper,
    private val preferencesManager: SharedPreferencesManager,
) : TasksRepository {
    override suspend fun getTaskLists(): List<TaskListDomainModel> {
        val taskListsResponse = tasksApi.getAllTaskLists()

        taskListsResponse.items?.let {
            return mapper.mapNotNull(it)
        }

        throw RuntimeException(taskListsResponse.error?.message)
    }

    override suspend fun createTaskList(name: String): TaskListDomainModel {
        val taskList = tasksApi.insertTaskList(TaskList(name = name))

        taskList?.let {
            return mapper.mapNotNull(it)
        }

        throw RuntimeException("Unable to create task list")
    }

    override suspend fun deleteTaskList(id: String) {
        tasksApi.removeTaskList(id)
    }

    override suspend fun getTodayTasks(): List<TaskDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksGroupBySubject(): List<TaskDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasks(): List<TaskDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun editTask(task: TaskDomainModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(task: TaskDomainModel) {
        TODO("Not yet implemented")
    }

    override fun isAuthorized(): Boolean {
        val accessToken = preferencesManager.getString(PREF_GOOGLE_ACCESS_TOKEN_KEY, "")
        val refreshToken = preferencesManager.getString(PREF_GOOGLE_REFRESH_TOKEN_KEY, "")

        return accessToken != null && refreshToken != null && accessToken != "" && refreshToken != ""
    }
}