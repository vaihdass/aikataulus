package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.EventDomainModel

interface EventsRepository {
    suspend fun getTodayEvents(calendarIds: List<Int>): List<EventDomainModel>

    suspend fun getEvents(calendarIds: List<Int>, from: String, to: String): List<EventDomainModel>
}