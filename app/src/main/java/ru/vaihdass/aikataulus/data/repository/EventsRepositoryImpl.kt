package ru.vaihdass.aikataulus.data.repository

import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.local.db.dao.EventDao
import ru.vaihdass.aikataulus.data.mapper.EventMapper
import ru.vaihdass.aikataulus.data.remote.api.AikataulusApi
import ru.vaihdass.aikataulus.domain.model.EventDomainModel
import ru.vaihdass.aikataulus.domain.repository.EventsRepository
import ru.vaihdass.aikataulus.utils.ResManager
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class EventsRepositoryImpl @Inject constructor(
    private val aikataulusApi: AikataulusApi,
    private val eventDao: EventDao,
    private val eventMapper: EventMapper,
    private val resManager: ResManager,
) : EventsRepository {
    override suspend fun getTodayEvents(calendarIds: List<Int>): List<EventDomainModel> {
        val (start, end) = getStartAndEndOfDay()

        return try {
            val events = aikataulusApi.getTodayEvents(calendarIds)?.let { list ->
                list.filter {
                    (it.dateFrom.time in start..end)
                            || (it.dateTo.time in start..end)
                }
            } ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_today_tasks))

            eventDao.deleteAllFromTo(start, end)
            eventDao.insertOrUpdateAll(eventMapper.mapToDbEntitiesNotNull(events))

            eventDao.getAllFromTo(start, end).map { eventMapper.mapNotNull(it) }
        } catch (e: Exception) {
            eventDao.getAllFromTo(start, end).map { eventMapper.mapNotNull(it) }
        }
    }

    override suspend fun getAllEvents(calendarIds: List<Int>): List<EventDomainModel> {
        return try {
            val events = aikataulusApi.getAllEvents(calendarIds)
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_get_all_events))

            eventDao.deleteAll()
            eventDao.insertOrUpdateAll(eventMapper.mapToDbEntitiesNotNull(events))

            eventDao.getAll().map { eventMapper.mapNotNull(it) }
        } catch (e: Exception) {
            eventDao.getAll().map { eventMapper.mapNotNull(it) }
        }
    }

    override suspend fun getEvents(
        calendarIds: List<Int>, from: Date, to: Date
    ): List<EventDomainModel> {
        val (start, end) = Pair(from.time, to.time)

        if (start > end)
            throw IllegalArgumentException(resManager.getString(R.string.incorrect_peek_event_from_to_date))

        try {
            val events = aikataulusApi.peekEvents(calendarIds, start, end)
                ?: throw RuntimeException(resManager.getString(R.string.unable_to_peek_events))

            eventDao.deleteAllFromTo(start, end)
            eventDao.insertOrUpdateAll(eventMapper.mapToDbEntitiesNotNull(events))

            return eventDao.getAllFromTo(start, end).map { eventMapper.mapNotNull(it) }
        } catch (e: Exception) {
            return eventDao.getAllFromTo(start, end).map { eventMapper.mapNotNull(it) }
        }
    }

    private fun getStartAndEndOfDay(): Pair<Long, Long> {
        val zoneId = ZoneId.systemDefault()

        val startOfDay = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli()
        val endOfDay =
            LocalDate.now().atTime(LocalTime.MAX).atZone(zoneId).toInstant().toEpochMilli()

        return Pair(startOfDay, endOfDay)
    }
}