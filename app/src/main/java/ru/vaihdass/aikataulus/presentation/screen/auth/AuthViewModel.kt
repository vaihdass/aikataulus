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
import ru.vaihdass.aikataulus.data.auth.AuthRepository
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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

    fun onAuthCodeFailed(exception: AuthorizationException) {
        _toastEventChannel.trySendBlocking(R.string.auth_cancelled)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        // TODO: LOG
        Timber.tag("oauth123").d("3. Received code = ${tokenRequest.authorizationCode}")

        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching {
                // TODO: LOG
                Timber.tag("oauth123").d("4. Change code to token. Url = ${tokenRequest.configuration.tokenEndpoint}, verifier = ${tokenRequest.codeVerifier}")
                authRepository.performTokenRequest(
                    authService = authService,
                    tokenRequest = tokenRequest
                )
            }.onSuccess {
                _loadingFlow.value = false
                _authSuccessEventChannel.send(Unit)
            }.onFailure {
                // TODO: LOG
                Timber.tag("oauth123").d("auth failed: $it")

                _loadingFlow.value = false
                _toastEventChannel.send(R.string.auth_cancelled)
            }
        }
    }

    fun openLoginPage() {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val authRequest = authRepository.getAuthRequest()

        // TODO: LOG
        Timber.tag("oauth123").d("1. Generated verifier=${authRequest.codeVerifier},challenge=${authRequest.codeVerifierChallenge}")

        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )

        _openAuthPageEventChannel.trySendBlocking(openAuthPageIntent)

        // TODO: LOG
        Timber.tag("oauth123").d("2. Open auth page: ${authRequest.toUri()}")
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}