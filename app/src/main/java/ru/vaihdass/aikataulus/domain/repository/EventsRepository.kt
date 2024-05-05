package ru.vaihdass.aikataulus.domain.repository

import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import java.util.Date

interface EventsRepository {
    suspend fun getTodayEvents(calendarIds: List<Int>): List<EventDomainModel>
    suspend fun getAllEvents(calendarIds: List<Int>): List<EventDomainModel>
    suspend fun getEvents(calendarIds: List<Int>, from: Date, to: Date): List<EventDomainModel>
}