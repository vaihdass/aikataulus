package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.mapper.toUiModelList
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import ru.vaihdass.aikataulus.presentation.model.TaskUiModel
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TasksRepository,
) {

    suspend fun getAllTasks(): List<TaskUiModel> {
        return withContext(dispatcher) {
            repository.getAllTasks().toUiModelList()
        }
    }

    suspend fun getTodayTasks(): List<TaskUiModel> {
        return withContext(dispatcher) {
            repository.getTodayTasks().toUiModelList()
        }
    }

    suspend fun getTasksGroupBySubject(): Map<String, List<TaskUiModel>> {
        return withContext(dispatcher) {
            repository.getTasksGroupBySubject().mapValues { entry -> entry.value.toUiModelList() }
        }
    }
}