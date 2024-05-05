package ru.vaihdass.aikataulus.presentation.screen.taskcreate

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.CreateTaskUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class TaskCreateViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val resManager: ResManager,
) : BaseViewModel() {
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _taskFlow = MutableSharedFlow<TaskUiModel?>(0)

    val errorFlow
        get() = _errorFlow.asSharedFlow()
    val taskFlow
        get() = _taskFlow.asSharedFlow()

    fun createTask(task: TaskUiModel) {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                createTaskUseCase.invoke(task)
            }.onSuccess { task ->
                _taskFlow.emit(task)
            }.onFailure { _ ->
                _errorFlow.emit(resManager.getString(R.string.unable_create_new_task))
                _errorFlow.emit(null)
            }
        }
    }
}