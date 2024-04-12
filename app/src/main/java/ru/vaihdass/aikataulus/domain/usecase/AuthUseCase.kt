package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.base.Constants.GOOGLE_TASKS_LIST_NAME
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_TASKS_LIST_ID
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import ru.vaihdass.aikataulus.domain.repository.AuthRepository
import ru.vaihdass.aikataulus.domain.repository.TasksRepository
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val aikataulusRepository: AikataulusRepository,
    private val tasksRepository: TasksRepository,
    private val preferencesManager: SharedPreferencesManager,
    private val resManager: ResManager,
) {
    operator fun invoke() = tasksRepository.isAuthorized()

    suspend fun performTokenRequest(authService: AuthorizationService, tokenRequest: TokenRequest) {
        withContext(dispatcher) {
            authRepository.performTokenRequest(
                authService = authService,
                tokenRequest = tokenRequest
            )
        }
    }

    fun getAuthRequest() = authRepository.getAuthRequest()
    suspend fun setOrCreateAikataulusTaskList(): Boolean {
        if (tasksRepository.isAuthorized().not()) return false

        return withContext(dispatcher) {
            val taskLists = tasksRepository.getTaskLists().filter { taskList -> taskList.name == GOOGLE_TASKS_LIST_NAME }

            if (taskLists.size != 1) createSyncTaskList()
            else {
                preferencesManager.putString(PREF_GOOGLE_TASKS_LIST_ID, taskLists[0].id)
            }

            val taskListId = preferencesManager.getString(PREF_GOOGLE_TASKS_LIST_ID, "")
            taskListId != null && taskListId.isEmpty().not()
        }
    }

    private suspend fun createSyncTaskList() {
        val id = tasksRepository.createTaskList(GOOGLE_TASKS_LIST_NAME).id
        if (id.trim().isEmpty()) throw RuntimeException(resManager.getString(R.string.auth_failed_tasklist))

        preferencesManager.putString(PREF_GOOGLE_TASKS_LIST_ID, id)
    }
}