package ru.vaihdass.aikataulus.domain.mapper

import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import ru.vaihdass.aikataulus.presentation.model.EventUiModel

fun EventDomainModel.toUiModel() = EventUiModel(
    subject = this.subject,
    location = this.location,
    type = this.type,
    teacher = this.teacher,
    calendarId = this.calendarId,
    calendarName = this.calendarName,
    dateFrom = this.dateFrom,
    dateTo = this.dateTo
)

fun List<EventDomainModel>.toUiModelList() = this.map { it.toUiModel() }