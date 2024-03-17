package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.remote.pojo.Event
import ru.vaihdass.aikataulus.data.remote.pojo.EventsResponse
import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import javax.inject.Inject

class EventDomainModelMapper @Inject constructor() {
    fun map(response: EventsResponse?): List<EventDomainModel>? {
        return response?.let {
            it.events?.map { event ->
                EventDomainModel(
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
    }

    fun map(input: Event?): EventDomainModel? {
        return input?.let { event ->
            EventDomainModel(
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

    fun map(input: EventDomainModel?): Event? {
        return input?.let { event ->
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
}