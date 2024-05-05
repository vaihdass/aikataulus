package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.mapper.EventMapper
import ru.vaihdass.aikataulus.data.remote.api.AikataulusApi
import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import ru.vaihdass.aikataulus.domain.repository.EventsRepository
import ru.vaihdass.aikataulus.utils.ResManager
import java.util.Date
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val aikataulusApi: AikataulusApi,
    private val eventMapper: EventMapper,
    private val resManager: ResManager,
) : EventsRepository {
    override suspend fun getTodayEvents(calendarIds: List<Int>): List<EventDomainModel> {
        aikataulusApi.getTodayEvents(calendarIds)?.let {
            return eventMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_get_today_tasks))
    }

    override suspend fun getAllEvents(calendarIds: List<Int>): List<EventDomainModel> {
        aikataulusApi.getAllEvents(calendarIds)?.let {
            return eventMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_get_all_events))
    }

    override suspend fun getEvents(
        calendarIds: List<Int>,
        from: Date,
        to: Date
    ): List<EventDomainModel> {
        aikataulusApi.peekEvents(calendarIds, from.time, to.time)?.let {
            return eventMapper.mapNotNull(it)
        }

        throw RuntimeException(resManager.getString(R.string.unable_to_peek_events))
    }
}