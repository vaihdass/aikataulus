package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.remote.pojo.Task
import ru.vaihdass.aikataulus.data.remote.pojo.TasksResponse
import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import javax.inject.Inject

class TaskDomainModelMapper @Inject constructor() {
    fun map(response: TasksResponse?): List<TaskDomainModel>? {
        return response?.let {
            it.tasks?.map { task ->
                TaskDomainModel(
                    task.id,
                    task.subject,
                    task.task,
                    task.description,
                    task.deadline,
                    task.status,
                )
            }
        }
    }

    fun map(input: Task?): TaskDomainModel? {
        return input?.let { task ->
            TaskDomainModel(
                task.id,
                task.subject,
                task.task,
                task.description,
                task.deadline,
                task.status,
            )
        }
    }

    fun map(input: TaskDomainModel?): Task? {
        return input?.let { task ->
            Task(
                task.id,
                task.subject,
                task.task,
                task.description,
                task.deadline,
                task.status,
            )
        }
    }
}