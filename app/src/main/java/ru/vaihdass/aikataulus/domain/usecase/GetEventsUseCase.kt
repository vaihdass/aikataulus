package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.mapper.toUiModelList
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import ru.vaihdass.aikataulus.domain.repository.EventsRepository
import ru.vaihdass.aikataulus.presentation.model.EventUiModel
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val eventsRepository: EventsRepository,
    private val aikataulusRepository: AikataulusRepository,
) {
    suspend fun getTodayEvents(): List<EventUiModel> {
        return withContext(dispatcher) {
            val calendars = aikataulusRepository.getAllSavedCalendarIds()

            eventsRepository.getTodayEvents(calendarIds = calendars).toUiModelList()
        }
    }

    suspend fun getAllEvents(): List<EventUiModel> {
        return withContext(dispatcher) {
            val calendars = aikataulusRepository.getAllSavedCalendarIds()

            eventsRepository.getAllEvents(calendarIds = calendars).toUiModelList()
        }
    }
}