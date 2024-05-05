package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.mapper.toUiModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import ru.vaihdass.aikataulus.presentation.mapper.toDomainModel
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import javax.inject.Inject

class EditTaskUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TasksRepository,
) {

    suspend operator fun invoke(task: TaskUiModel): TaskUiModel {
        return withContext(dispatcher) {
            repository.editTask(task.toDomainModel()).toUiModel()
        }
    }
}