package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.mapper.toUiModelList
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import javax.inject.Inject

class RemoveDoneTasksUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(): List<TaskUiModel> {
        return withContext(dispatcher) {
            repository.removeDoneTasks()
            repository.getAllTasks().toUiModelList()
        }
    }
}