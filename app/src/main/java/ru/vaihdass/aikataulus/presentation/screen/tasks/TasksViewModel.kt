package ru.vaihdass.aikataulus.presentation.screen.tasks

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.GetTasksUseCase
import ru.vaihdass.aikataulus.domain.usecase.RemoveDoneTasksUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.presentation.recyclerview.adapter.TasksAdapter
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val removeDoneTasksUseCase: RemoveDoneTasksUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val resManager: ResManager,
) : BaseViewModel() {
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _tasksFlow = MutableStateFlow<List<TaskUiModel>>(emptyList())
    private var _tasksAdapter: TasksAdapter? = null

    val errorFlow
        get() = _errorFlow.asSharedFlow()
    val tasksFlow
        get() = _tasksFlow.asStateFlow()
    var tasksAdapter
        get() = _tasksAdapter ?: throw IllegalStateException("Events adapter should be initialized")
        set(value) {
            if (_tasksAdapter == null) {
                _tasksAdapter = value
            }
        }

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getTasksUseCase.getAllTasks()
            }.onSuccess { tasks ->
                _tasksFlow.value = tasks
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_get_all_tasks))
                _errorFlow.emit(null)
            }
        }
    }

    fun removeDoneTasks() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                removeDoneTasksUseCase.invoke()
            }.onSuccess { tasks ->
                _tasksFlow.value = tasks
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_remove_done_tasks))
                _errorFlow.emit(null)
            }
        }
    }
}