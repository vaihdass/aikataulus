package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.remote.pojo.tasks.TaskList
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import javax.inject.Inject

class TaskListMapper @Inject constructor() {
    fun map(response: List<TaskList>?): List<TaskListDomainModel>? {
        return response?.let { mapNotNull(response) }
    }

    fun mapNotNull(response: List<TaskList>): List<TaskListDomainModel> {
        return response.map { taskList -> mapNotNull(taskList) }
    }

    fun map(input: TaskList?): TaskListDomainModel? {
        return input?.let { taskList -> mapNotNull(taskList) }
    }

    fun mapNotNull(taskList: TaskList): TaskListDomainModel {
        return TaskListDomainModel(
            id = taskList.id,
            name = taskList.name,
            updated = taskList.updated,
        )
    }

    fun map(taskListDomainModel: TaskListDomainModel?): TaskList? {
        return taskListDomainModel?.let { taskList ->
            TaskList(
                id = taskList.id,
                name = taskList.name,
                updated = taskList.updated,
            )
        }
    }
}