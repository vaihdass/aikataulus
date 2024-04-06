package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel

interface TasksRepository {
    suspend fun getTaskLists(): List<TaskListDomainModel>
    suspend fun createTaskList(name: String): TaskListDomainModel
    suspend fun deleteTaskList(id: String)
    suspend fun getTodayTasks(): List<TaskDomainModel>
    suspend fun getTasksGroupBySubject(): List<TaskDomainModel>
    suspend fun getAllTasks(): List<TaskDomainModel>
    suspend fun editTask(task: TaskDomainModel)
    suspend fun deleteTask(taskId: String)
    suspend fun createTask(task: TaskDomainModel)
    fun isAuthorized(): Boolean
}