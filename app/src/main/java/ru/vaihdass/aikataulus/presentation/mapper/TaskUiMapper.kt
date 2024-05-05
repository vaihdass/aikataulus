package ru.vaihdass.aikataulus.presentation.mapper

import ru.vaihdass.aikataulus.domain.model.TaskDomainModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel

fun TaskUiModel.toDomainModel() = TaskDomainModel(
    id = this.id,
    subject = this.subject,
    task = this.task,
    deadline = this.deadline,
    isDone = this.isDone
)

fun List<TaskUiModel>.toDomainModelList() = this.map { it.toDomainModel() }