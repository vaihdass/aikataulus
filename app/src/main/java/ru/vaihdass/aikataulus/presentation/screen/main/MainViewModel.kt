package ru.vaihdass.aikataulus.presentation.screen.main

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
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val getEventsUseCase: GetEventsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val resManager: ResManager,
) : BaseViewModel() {
    private val _errorFlow = MutableStateFlow<String?>(null)
    private val _eventsFlow = MutableStateFlow<List<EventUiModel>>(emptyList())
    private val _tasksFlow = MutableStateFlow<List<TaskUiModel>>(emptyList())

    private var _eventsAdapter: TodayEventsAdapter? = null
    private var _tasksAdapter: TodayTasksAdapter? = null

    val errorFlow
        get() = _errorFlow.asStateFlow()
    val eventsFlow
        get() = _eventsFlow.asStateFlow()
    val tasksFlow
        get() = _tasksFlow.asStateFlow()
    val eventsAdapter
        get() = _eventsAdapter ?: throw IllegalStateException("Events adapter should be initialized")

    val tasksAdapter
        get() = _tasksAdapter ?: throw IllegalStateException("Events adapter should be initialized")

    fun initEventsAdapter(onItemClicked: (EventUiModel) -> Unit) {
        if (_eventsAdapter != null) return

        _eventsAdapter = TodayEventsAdapter(onItemClicked)
    }

    fun initTasksAdapter(onItemClicked: (TaskUiModel) -> Unit) {
        if (_tasksAdapter != null) return

        _tasksAdapter = TodayTasksAdapter(onItemClicked)
    }

    init {
        fetchEvents()
        fetchTasks()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getEventsUseCase.getTodayEvents()
            }.onSuccess { events ->
                _eventsFlow.value = events
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_get_events_for_today))
                _errorFlow.emit(null)
            }
        }
    }

    fun fetchTasks() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getTasksUseCase.getTodayTasks()
            }.onSuccess { tasks ->
                _tasksFlow.value = tasks
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_get_tasks_for_today))
                _errorFlow.emit(null)
            }
        }
    }
}