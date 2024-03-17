package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel

interface TasksRepository {
    suspend fun getTodayTasks(): List<TaskDomainModel>
    suspend fun getAllTasks(): List<TaskDomainModel>
    suspend fun editTask(task: TaskDomainModel)
    suspend fun deleteTask(taskId: String)
    suspend fun createTask(task: TaskDomainModel)
}