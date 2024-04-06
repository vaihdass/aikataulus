package ru.vaihdass.aikataulus.data.remote.api

import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.TokenRequest
import ru.vaihdass.aikataulus.data.auth.model.TokensModel

interface AuthApi {
    fun getAuthRequest(): AuthorizationRequest
    fun getEndSessionRequest(): EndSessionRequest
    fun getRefreshTokenRequest(refreshToken: String): TokenRequest
    suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel
}