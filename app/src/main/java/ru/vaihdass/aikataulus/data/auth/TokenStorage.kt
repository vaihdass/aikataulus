package ru.vaihdass.aikataulus.data.auth

import ru.vaihdass.aikataulus.base.Constants.EMPTY_VALUE_STRING
import ru.vaihdass.aikataulus.base.Constants.GOOGLE_ACCESS_TOKEN_KEY
import ru.vaihdass.aikataulus.base.Constants.GOOGLE_ID_TOKEN_KEY
import ru.vaihdass.aikataulus.base.Constants.GOOGLE_REFRESH_TOKEN_KEY
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import javax.inject.Inject

class TokenStorage @Inject constructor(
    private val prefsManager: SharedPreferencesManager
) {
    var accessToken: String?
        get() = prefsManager.getString(GOOGLE_ACCESS_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(GOOGLE_ACCESS_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)

    var refreshToken: String?
        get() = prefsManager.getString(GOOGLE_REFRESH_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(GOOGLE_REFRESH_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)

    var idToken: String?
        get() = prefsManager.getString(GOOGLE_ID_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(GOOGLE_ID_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)
}