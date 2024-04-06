package ru.vaihdass.aikataulus.domain.mapper

import ru.vaihdass.aikataulus.domain.model.CalendarDomainModel
import ru.vaihdass.aikataulus.presentation.model.CalendarUiModel

fun CalendarDomainModel.toUiModel() = CalendarUiModel(
    id = this.id,
    url = this.url,
    name = this.name,
    courseId = this.courseId
)

fun List<CalendarDomainModel>.toUiModelList() = this.map { it.toUiModel() }

fun CalendarUiModel.toDomainModel() = CalendarDomainModel(
    id = this.id,
    url = this.url,
    name = this.name,
    courseId = this.courseId
)

fun List<CalendarUiModel>.toDomainModelList() = this.map { it.toDomainModel() }