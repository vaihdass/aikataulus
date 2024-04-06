package ru.vaihdass.aikataulus.data.mapper

import ru.vaihdass.aikataulus.data.local.db.entity.CalendarEntity
import ru.vaihdass.aikataulus.data.remote.pojo.aikataulus.Calendar
import ru.vaihdass.aikataulus.domain.model.CalendarDomainModel
import javax.inject.Inject

class CalendarMapper @Inject constructor() {
    fun map(response: List<Calendar>?): List<CalendarDomainModel>? {
        return response?.let {
            it.map { calendar -> mapNotNull(calendar) }
        }
    }

    fun map(input: Calendar?): CalendarDomainModel? {
        return input?.let { calendar -> mapNotNull(calendar) }
    }

    fun mapNotNull(calendar: Calendar): CalendarDomainModel {
        return CalendarDomainModel(
            id = calendar.id,
            url = calendar.url,
            name = calendar.name,
            courseId = calendar.courseId,
        )
    }

    fun map(calendarDomainModel: CalendarDomainModel?): Calendar? {
        return calendarDomainModel?.let { calendar ->
            Calendar(
                id = calendar.id,
                url = calendar.url,
                name = calendar.name,
                courseId = calendar.courseId,
            )
        }
    }

    fun mapToDbEntityNotNull(calendar: Calendar): CalendarEntity {
        return CalendarEntity(
            id = calendar.id,
            url = calendar.url,
            name = calendar.name,
            courseId = calendar.courseId,
        )
    }

    fun mapToDbEntities(response: List<Calendar>?): List<CalendarEntity>? {
        return response?.let {
            it.map { calendar -> mapToDbEntityNotNull(calendar) }
        }
    }

    fun map(calendar: CalendarEntity): CalendarDomainModel {
        return CalendarDomainModel(
            id = calendar.id,
            url = calendar.url,
            name = calendar.name,
            courseId = calendar.courseId,
        )
    }

    fun fromDbEntities(calendars: List<CalendarEntity>): List<CalendarDomainModel> {
        return calendars.map { calendar -> map(calendar) }
    }

    fun mapDomainToDbEntities(calendars: List<CalendarDomainModel>): List<CalendarEntity> {
        return calendars.map { calendar ->
            CalendarEntity(
                id = calendar.id,
                url = calendar.url,
                name = calendar.name,
                courseId = calendar.courseId,
            )
        }
    }
}