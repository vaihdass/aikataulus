package ru.vaihdass.aikataulus.data.auth

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val tokenStorage: TokenStorage,
    private val appAuth: AppAuth,
) {
    fun corruptAccessToken() {
        tokenStorage.accessToken = INCORRECT_ACCESS_TOKEN_VALUE
    }

    fun logout() {
        tokenStorage.accessToken = null
        tokenStorage.refreshToken = null
        tokenStorage.idToken = null
    }

    fun getAuthRequest(): AuthorizationRequest {
        return appAuth.getAuthRequest()
    }

    fun getEndSessionRequest(): EndSessionRequest {
        return appAuth.getEndSessionRequest()
    }

    suspend fun performTokenRequest(authService: AuthorizationService, tokenRequest: TokenRequest) {
        val tokens = appAuth.performTokenRequestSuspend(authService, tokenRequest)

        tokenStorage.accessToken = tokens.accessToken.trim()
        tokenStorage.refreshToken = tokens.refreshToken.trim()
        tokenStorage.idToken = tokens.idToken.trim()

        // TODO: Log
        Timber.tag("Oauth").d("6. Tokens accepted:\n access=${tokens.accessToken}\nrefresh=${tokens.refreshToken}\nidToken=${tokens.idToken}")
    }

    companion object {
        const val INCORRECT_ACCESS_TOKEN_VALUE = "INCORRECT_ACCESS_TOKEN_VALUE"
    }
}