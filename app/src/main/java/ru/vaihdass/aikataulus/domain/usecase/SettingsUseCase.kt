package ru.vaihdass.aikataulus.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import ru.vaihdass.aikataulus.domain.repository.AuthRepository
import javax.inject.Inject

class SettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val aikataulusRepository: AikataulusRepository,
) {
    suspend fun logout() {
        withContext(dispatcher) {
            authRepository.logout()
            aikataulusRepository.clearEntities()
            aikataulusRepository.setChoseCalendars(value = false)
        }
    }

    fun getLogoutRequest() = authRepository.getEndSessionRequest()
}