package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.data.mapper.TaskListMapper
import ru.vaihdass.aikataulus.data.remote.api.TasksApi
import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import timber.log.Timber
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val tasksApi: TasksApi,
    private val mapper: TaskListMapper,
) : TasksRepository {
    override suspend fun getTaskLists(): List<TaskListDomainModel> {
        val taskListsResponse = tasksApi.getAllTaskLists()

        Timber.tag("tasks_api").d(taskListsResponse.toString())

        taskListsResponse.items?.let {
            return mapper.mapNotNull(it)
        }

        throw RuntimeException(taskListsResponse.error?.message)
    }

    override suspend fun getTodayTasks(): List<TaskDomainModel> {
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
}