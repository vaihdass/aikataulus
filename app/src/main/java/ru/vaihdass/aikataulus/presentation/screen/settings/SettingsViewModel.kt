package ru.vaihdass.aikataulus.presentation.screen.settings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.data.ExceptionHandlerDelegate
import ru.vaihdass.aikataulus.data.runCatching
import ru.vaihdass.aikataulus.domain.usecase.SettingsUseCase
import ru.vaihdass.aikataulus.presentation.base.BaseViewModel
import ru.vaihdass.aikataulus.utils.ResManager
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val ctx: Context,
    private val settingsUseCase: SettingsUseCase,
    private val resManager: ResManager,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : BaseViewModel() {
    private val _errorFlow = MutableSharedFlow<String?>(1)
    private val _openShareScheduleIntentChannel = Channel<Intent>(Channel.BUFFERED)
    private val _openLogoutIntentChannel = Channel<Intent>(Channel.BUFFERED)
    private val _logoutSuccessChannel = Channel<Unit>(Channel.BUFFERED)
    private val authService: AuthorizationService = AuthorizationService(ctx)

    val errorFlow
        get() = _errorFlow.asSharedFlow()
    val openShareScheduleIntentFlow
        get() = _openShareScheduleIntentChannel.receiveAsFlow()
    val openLogoutIntentFlow
        get() = _openLogoutIntentChannel.receiveAsFlow()
    val logoutSuccessFlow
        get() = _logoutSuccessChannel.receiveAsFlow()

    fun getExportScheduleIntent(url: String, packageManager: PackageManager) {
        viewModelScope.launch {
            val exportIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            if (exportIntent.resolveActivity(packageManager) != null) {
                _openShareScheduleIntentChannel.trySendBlocking(exportIntent)
            } else {
                _errorFlow.emit(resManager.getString(R.string.unavailable_to_share_link))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                settingsUseCase.logout()
            }.onSuccess {
                _logoutSuccessChannel.trySendBlocking(Unit)
            }.onFailure { throwable ->
                _errorFlow.emit(throwable.message)
            }
        }
    }

    fun createLogoutPageIntent() {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val logoutRequest = settingsUseCase.getLogoutRequest()

        val openLogoutPageIntent = authService.getEndSessionRequestIntent(
            logoutRequest,
            customTabsIntent
        )

        _openLogoutIntentChannel.trySendBlocking(openLogoutPageIntent)
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}