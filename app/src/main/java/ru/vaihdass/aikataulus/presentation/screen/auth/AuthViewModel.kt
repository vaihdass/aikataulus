package ru.vaihdass.aikataulus.presentation.screen.auth

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.domain.usecase.AuthUseCase
import ru.vaihdass.aikataulus.domain.usecase.GreetingUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val greetingUseCase: GreetingUseCase,
    private val authService: AuthorizationService
) : BaseViewModel() {

    private val _openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val _toastEventChannel = Channel<Int>(Channel.BUFFERED)
    private val _authSuccessEventChannel = Channel<Unit>(Channel.BUFFERED)
    private val _loadingFlow = MutableStateFlow(false)

    val openAuthPageFlow: Flow<Intent>
        get() = _openAuthPageEventChannel.receiveAsFlow()
    val loadingFlow: StateFlow<Boolean>
        get() = _loadingFlow.asStateFlow()
    val toastFlow: Flow<Int>
        get() = _toastEventChannel.receiveAsFlow()
    val authSuccessFlow: Flow<Unit>
        get() = _authSuccessEventChannel.receiveAsFlow()

    fun isAuthorized() = authUseCase.invoke()

    fun choseCalendars() = greetingUseCase.choseCalendars()

    fun onAuthCodeFailed(exception: AuthorizationException) {
        _toastEventChannel.trySendBlocking(R.string.auth_cancelled)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            _loadingFlow.value = true
            try {
                val tokenResponse = authUseCase.performTokenRequest(
                    authService = authService,
                    tokenRequest = tokenRequest
                )

                try {
                    val taskListCreated = authUseCase.setOrCreateAikataulusTaskList()

                    if (taskListCreated) _authSuccessEventChannel.send(Unit)
                    else _toastEventChannel.send(R.string.auth_failed_tasklist)
                } catch (e: Exception) {
                    _toastEventChannel.send(R.string.auth_failed_tasklist)
                }
            } catch (e: Exception) {
                _toastEventChannel.send(R.string.auth_cancelled)
            } finally {
                _loadingFlow.value = false
            }
        }
    }

    fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val authRequest = authUseCase.getAuthRequest()

        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest, customTabsIntent
        )

        _openAuthPageEventChannel.trySendBlocking(openAuthPageIntent)

    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}