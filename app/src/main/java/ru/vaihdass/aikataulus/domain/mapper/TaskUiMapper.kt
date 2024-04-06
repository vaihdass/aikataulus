package ru.vaihdass.aikataulus.domain.mapper

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel

fun TaskDomainModel.toUiModel() = TaskUiModel(
    id = this.id,
    subject = this.subject,
    task = this.task,
    deadline = this.deadline,
    isDone = this.isDone
)

fun List<TaskDomainModel>.toUiModelList() = this.map { it.toUiModel() }