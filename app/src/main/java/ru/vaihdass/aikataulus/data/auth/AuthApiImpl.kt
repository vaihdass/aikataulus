package ru.vaihdass.aikataulus.data.auth

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import ru.vaihdass.aikataulus.BuildConfig
import ru.vaihdass.aikataulus.data.auth.model.TokensModel
import ru.vaihdass.aikataulus.data.remote.api.AuthApi
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthApiImpl @Inject constructor() : AuthApi {
    private val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(GoogleAuthConfig.AUTH_URI),
        Uri.parse(GoogleAuthConfig.TOKEN_URI),
        null,
        Uri.parse(GoogleAuthConfig.LOGOUT_URI)
    )

    override fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = GoogleAuthConfig.CALLBACK_URL.toUri()

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            GoogleAuthConfig.CLIENT_ID,
            GoogleAuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setScope(GoogleAuthConfig.SCOPES)
            .build()
    }

    override fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(GoogleAuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }

    override fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            GoogleAuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setScopes(GoogleAuthConfig.SCOPES)
            .setRefreshToken(refreshToken)
            .build()
    }

    override suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, e ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }

                    e != null -> {
                        continuation.resumeWith(Result.failure(e))
                    }
                    else -> error("Unreachable")
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(GoogleAuthConfig.CLIENT_SECRET)
    }

    private object GoogleAuthConfig {
        const val AUTH_URI = "https://accounts.google.com/o/oauth2/auth"
        const val TOKEN_URI = "https://oauth2.googleapis.com/token"
        const val LOGOUT_URI = "https://accounts.google.com/logout"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val SCOPES = "https://www.googleapis.com/auth/tasks https://www.googleapis.com/auth/tasks.readonly"
        const val CLIENT_ID = BuildConfig.GOOGLE_OAUTH_CLIENT_ID
        const val CLIENT_SECRET = "" // Google oAuth doesn't require it anymore
        const val CALLBACK_URL = "ru.vaihdass.aikataulus:/google.com/callback"
        const val LOGOUT_CALLBACK_URL = "ru.vaihdass.aikataulus://google.com/logout_callback"
    }
}