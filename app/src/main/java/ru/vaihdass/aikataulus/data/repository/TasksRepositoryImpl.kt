package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository

class TasksRepositoryImpl : TasksRepository {
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