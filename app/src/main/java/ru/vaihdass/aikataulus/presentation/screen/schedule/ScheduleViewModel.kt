package ru.vaihdass.aikataulus.presentation.screen.schedule

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.GetEventsUseCase
import ru.vaihdass.aikataulus.domain.usecase.GetTasksUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.EventUiModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TodayEventsAdapter
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TodayTasksAdapter
import ru.vaihdass.aikataulus.utils.ResManager
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class ScheduleViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val resManager: ResManager,
) : BaseViewModel() {
    private val _errorFlow = MutableStateFlow<String?>(null)
    private val _eventsFlow = MutableStateFlow<List<EventUiModel>>(emptyList())
    private val _allEventsFlow = MutableStateFlow<List<EventUiModel>>(emptyList())
    private val _tasksFlow = MutableStateFlow<List<TaskUiModel>>(emptyList())
    private val _allTasksFlow = MutableStateFlow<List<TaskUiModel>>(emptyList())

    private var _eventsAdapter: TodayEventsAdapter? = null
    private var _tasksAdapter: TodayTasksAdapter? = null

    private val allEvents: MutableList<EventUiModel> = mutableListOf()
    private val allTasks: MutableList<TaskUiModel> = mutableListOf()

    val errorFlow
        get() = _errorFlow.asStateFlow()
    val eventsFlow
        get() = _eventsFlow.asStateFlow()
    val allEventsFlow
        get() = _allEventsFlow.asStateFlow()
    val tasksFlow
        get() = _tasksFlow.asStateFlow()
    val allTasksFlow
        get() = _allTasksFlow.asStateFlow()

    var eventsAdapter
        get() = _eventsAdapter
            ?: throw IllegalStateException("Events adapter should be initialized")
        set(value) {
            if (_eventsAdapter != null) return

            _eventsAdapter = value
        }
    var tasksAdapter
        get() = _tasksAdapter
            ?: throw IllegalStateException("Tasks adapter should be initialized")
        set(value) {
            if (_tasksAdapter != null) return

            _tasksAdapter = value
        }

    init {
        fetchEvents()
        fetchTasks()
    }

    fun fetchEventsByDate(date: LocalDate) {
        viewModelScope.launch {
            val eventsByDate = allEvents
                .filter {
                    sameDate(it.dateFrom, date) || sameDate(it.dateTo, date)
                }.sortedBy { it.dateFrom }

            _eventsFlow.emit(eventsByDate)
        }
    }

    fun fetchTasksByDate(date: LocalDate) {
        viewModelScope.launch {
            val tasksByDate = allTasks
                .filter {
                    it.deadline?.let { deadline -> sameDate(deadline, date) } ?: false
                }.sortedBy { it.deadline }

            _tasksFlow.emit(tasksByDate)
        }
    }

    fun hasEvents(date: LocalDate) = allEvents.none {
        sameDate(it.dateFrom, date) || sameDate(it.dateTo, date)
    }.not()

    fun hasTasks(date: LocalDate) = allTasks.none {
        it.deadline?.let { deadline -> sameDate(deadline, date) } ?: false
    }.not()

    private fun fetchEvents() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getEventsUseCase.getAllEvents()
            }.onSuccess { events ->
                allEvents.addAll(events)
                _allEventsFlow.emit(events)
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_get_all_events))
                _errorFlow.emit(null)
            }
        }
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getTasksUseCase.getAllTasks()
            }.onSuccess { tasks ->
                val tasksWithDeadline = tasks.filter { it.deadline != null }

                allTasks.addAll(tasksWithDeadline)
                _allTasksFlow.emit(tasksWithDeadline)
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_get_tasks_for_today))
                _errorFlow.emit(null)
            }
        }
    }

    private fun sameDate(date1: Date, date2: LocalDate): Boolean {
        val date1LocalDate = date1.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        return date2.isEqual(date1LocalDate)
    }
}