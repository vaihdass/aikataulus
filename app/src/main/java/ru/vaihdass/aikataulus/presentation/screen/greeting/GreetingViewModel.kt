package ru.vaihdass.aikataulus.presentation.screen.greeting

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import ru.vaihdass.aikataulus.domain.usecase.GetTasksListsUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import javax.inject.Inject

class GreetingViewModel @Inject constructor(
    private val getTasksListsUseCase: GetTasksListsUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    private val _stateFlow = MutableStateFlow<List<TaskListDomainModel>>(emptyList())
    val stateFlow = _stateFlow.asStateFlow()

    private val _errorFlow = MutableSharedFlow<String?>(1)
    val errorFlow: SharedFlow<String?>
        get() = _errorFlow.asSharedFlow()

    fun fetchTaskLists() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                getTasksListsUseCase.invoke()
            }.onSuccess { taskLists ->
                _stateFlow.value = taskLists
            }.onFailure { throwable ->
                _errorFlow.emit(throwable.message)
            }
        }
    }
}