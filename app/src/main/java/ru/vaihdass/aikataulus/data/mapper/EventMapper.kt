package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.OrganizationEntity
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Event
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Organization
import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import javax.inject.Inject

class EventMapper @Inject constructor() {
    fun map(response: List<Event>?): List<EventDomainModel>? {
        return response?.let {
            it.map { event -> mapNotNull(event) }
        }
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

    fun mapToDbEntityNotNull(organization: Organization): OrganizationEntity {
        return OrganizationEntity(
            id = organization.id,
            name = organization.name,
        )
    }

    fun mapToDbEntities(response: List<Organization>?): List<OrganizationEntity>? {
        return response?.let {
            it.map { organization -> mapToDbEntityNotNull(organization) }
        }
    }
}