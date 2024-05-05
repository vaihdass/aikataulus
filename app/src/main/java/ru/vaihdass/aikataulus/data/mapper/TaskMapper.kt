package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.TaskEntity
import ru.vaihdass.aikataulus.data.remote.pojo.tasks.Task
import ru.vaihdass.aikataulus.data.remote.util.TaskStatus
import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import javax.inject.Inject

class TaskMapper @Inject constructor() {
    fun map(response: List<Task>?): List<TaskDomainModel>? {
        return response?.let { mapNotNull(it) }
    }

    fun mapNotNull(response: List<Task>): List<TaskDomainModel> {
        return response.map { task -> mapNotNull(task) }
    }

    fun map(input: Task?): TaskDomainModel? {
        return input?.let { task -> mapNotNull(task) }
    }

    fun mapNotNull(task: Task): TaskDomainModel {
        return TaskDomainModel(
            id = task.id,
            subject = task.subject,
            task = task.task ?: "",
            deadline = task.deadline,
            isDone = task.status == TaskStatus.DONE,
        )
    }

    fun map(taskDomainModel: TaskDomainModel?): Task? {
        return taskDomainModel?.let { task -> mapNotNull(task) }
    }

    fun mapNotNull(taskDomainModel: TaskDomainModel): Task {
        return taskDomainModel.let { task ->
            Task(
                id = task.id,
                subject = task.subject,
                task = task.task,
                deadline = task.deadline,
                status = if (task.isDone) TaskStatus.DONE else TaskStatus.NOT_COMPLETED,
            )
        }
    }

    fun mapToDbEntityNotNull(task: Task): TaskEntity {
        return TaskEntity(
            id = task.id,
            subject = task.subject,
            task = task.task ?: "",
            deadline = task.deadline,
            isDone = task.status == TaskStatus.DONE,
        )
    }

    fun mapToDbEntityNotNull(task: TaskDomainModel, saved: Boolean = true): TaskEntity {
        return TaskEntity(
            id = task.id,
            subject = task.subject,
            task = task.task,
            deadline = task.deadline,
            isDone = task.isDone,
            saved = saved,
        )
    }

    fun mapToDbEntities(response: List<Task>?): List<TaskEntity>? {
        return response?.let {
            it.map { task -> mapToDbEntityNotNull(task) }
        }
    }

    fun fromDbEntities(tasks: List<TaskEntity>) = tasks.map { task ->
        TaskDomainModel(
            id = task.id,
            subject = task.subject,
            task = task.task,
            deadline = task.deadline,
            isDone = task.isDone,
        )
    }
}