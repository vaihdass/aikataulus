package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import ru.vaihdass.aikataulus.domain.repository.EventsRepository

class EventsRepositoryImpl : EventsRepository {
    override suspend fun getTodayEvents(calendarIds: List<Int>): List<EventDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(
        calendarIds: List<Int>,
        from: String,
        to: String
    ): List<EventDomainModel> {
        TODO("Not yet implemented")
    }
}