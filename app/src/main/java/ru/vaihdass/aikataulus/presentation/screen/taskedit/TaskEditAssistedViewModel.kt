package ru.vaihdass.aikataulus.presentation.screen.taskedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.EditTaskUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.utils.ResManager

class TaskEditAssistedViewModel @AssistedInject constructor(
    @Assisted(value = TASK_ASSISTED_KEY) var task: TaskUiModel,
    private val editTaskUseCase: EditTaskUseCase,
    private val resManager: ResManager,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _taskEditSuccessEventFlow = MutableSharedFlow<Unit>(1)

    val errorFlow
        get() = _errorFlow.asSharedFlow()
    val taskEditSuccessEventFlow
        get() = _taskEditSuccessEventFlow.asSharedFlow()

    fun editTask() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                editTaskUseCase.invoke(task)
            }.onSuccess {
                task = it
                _taskEditSuccessEventFlow.emit(Unit)
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_to_modify_task))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(TASK_ASSISTED_KEY) task: TaskUiModel,
        ): TaskEditAssistedViewModel
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