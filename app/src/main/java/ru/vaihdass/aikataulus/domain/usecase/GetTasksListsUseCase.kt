package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.model.TaskListDomainModel
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import javax.inject.Inject

class GetTasksListsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: TasksRepository,
) {

    suspend operator fun invoke(): List<TaskListDomainModel> {
        return withContext(dispatcher) {
            val taskLists = repository.getTaskLists()

            taskLists
        }
    }
}