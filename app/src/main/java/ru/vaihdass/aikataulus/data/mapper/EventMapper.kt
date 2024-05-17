package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.EventEntity
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Event
import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import javax.inject.Inject

class EventMapper @Inject constructor() {
    fun map(response: List<Event>?): List<EventDomainModel>? {
        return response?.let { mapNotNull(it) }
    }

    fun mapNotNull(response: List<Event>): List<EventDomainModel> {
        return response.map { event -> mapNotNull(event) }
    }

    fun map(input: Event?): EventDomainModel? {
        return input?.let { event -> mapNotNull(event) }
    }

    fun mapNotNull(event: Event): EventDomainModel {
        return EventDomainModel(
            event.subject,
            event.location,
            event.type,
            event.teacher,
            event.calendarId,
            event.calendarName,
            event.dateFrom,
            event.dateTo,
        )
    }

    fun mapNotNull(event: EventEntity): EventDomainModel {
        return EventDomainModel(
            event.subject,
            event.location,
            event.type,
            event.teacher,
            event.calendarId,
            event.calendarName,
            event.dateFrom,
            event.dateTo,
        )
    }

    fun map(eventDomainModel: EventDomainModel?): Event? {
        return eventDomainModel?.let { event ->
            Event(
                event.subject,
                event.location,
                event.type,
                event.teacher,
                event.calendarId,
                event.calendarName,
                event.dateFrom,
                event.dateTo,
            )
        }
    }

    fun mapToDbEntitiesNotNull(events: List<Event>): List<EventEntity> {
        return events.map { event ->
            EventEntity(
                id = 0,
                subject = event.subject,
                location = event.location,
                type = event.type,
                teacher = event.teacher,
                calendarId = event.calendarId,
                calendarName = event.calendarName,
                dateFrom = event.dateFrom,
                dateTo = event.dateTo,
            )
        }
    }
}