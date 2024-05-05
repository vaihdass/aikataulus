package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel

interface TasksRepository {
    suspend fun getTaskLists(): List<TaskListDomainModel>
    suspend fun createTaskList(name: String): TaskListDomainModel
    suspend fun deleteTaskList(id: String)
    suspend fun getTodayTasks(): List<TaskDomainModel>
    suspend fun getTasksGroupBySubject(): Map<String, List<TaskDomainModel>>
    suspend fun getAllTasks(): List<TaskDomainModel>
    suspend fun getTask(task: TaskDomainModel): TaskDomainModel
    suspend fun editTask(task: TaskDomainModel): TaskDomainModel
    suspend fun deleteTask(taskId: String)
    suspend fun removeDoneTasks()
    suspend fun createTask(task: TaskDomainModel): TaskDomainModel
    fun isAuthorized(): Boolean
}