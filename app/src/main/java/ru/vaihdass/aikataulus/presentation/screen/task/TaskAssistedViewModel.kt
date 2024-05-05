package ru.vaihdass.aikataulus.presentation.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.EditTaskUseCase
import ru.vaihdass.aikataulus.domain.usecase.GetTaskUseCase
import ru.vaihdass.aikataulus.domain.usecase.RemoveTaskUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.utils.ResManager

class TaskAssistedViewModel @AssistedInject constructor(
    @Assisted(value = TASK_ASSISTED_KEY) var task: TaskUiModel,
    private val getTaskUseCase: GetTaskUseCase,
    private val editTaskUseCase: EditTaskUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val resManager: ResManager,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _taskFlow = MutableStateFlow<TaskUiModel>(task)
    private val _removeSuccessEventFlow = MutableSharedFlow<Unit>(0)

    val errorFlow
        get() = _errorFlow.asSharedFlow()

    val taskFlow
        get() = _taskFlow.asStateFlow()

    val removeSuccessEventFlow
        get() = _removeSuccessEventFlow.asSharedFlow()

    fun fetchTask() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getTaskUseCase.invoke(task)
            }.onSuccess {
                task = it
                _taskFlow.value = it
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_get_this_task))
            }
        }
    }

    fun editTask() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                editTaskUseCase.invoke(task)
            }.onSuccess {
                task = it
                _taskFlow.value = it
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_modify_task))
            }
        }
    }

    fun removeTask() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                removeTaskUseCase.invoke(task)
            }.onSuccess {
                _removeSuccessEventFlow.emit(Unit)
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_delete_task))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(TASK_ASSISTED_KEY) task: TaskUiModel,
        ): TaskAssistedViewModel
    }

    companion object {
        const val TASK_ASSISTED_KEY = "TASK_ASSISTED_KEY"

        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: Factory,
            task: TaskUiModel,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(task) as T
            }
        }
    }
}