package ru.vaihdass.aikataulus.data.auth

import ru.vaihdass.aikataulus.base.Constants.EMPTY_VALUE_STRING
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_ACCESS_TOKEN_KEY
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_ID_TOKEN_KEY
import ru.vaihdass.aikataulus.base.Constants.PREF_GOOGLE_REFRESH_TOKEN_KEY
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager
import javax.inject.Inject

class TokenStorage @Inject constructor(
    private val prefsManager: SharedPreferencesManager
) {
    var accessToken: String?
        get() = prefsManager.getString(PREF_GOOGLE_ACCESS_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(PREF_GOOGLE_ACCESS_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)

    var refreshToken: String?
        get() = prefsManager.getString(PREF_GOOGLE_REFRESH_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(PREF_GOOGLE_REFRESH_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)

    var idToken: String?
        get() = prefsManager.getString(PREF_GOOGLE_ID_TOKEN_KEY, EMPTY_VALUE_STRING)
        set(value) = prefsManager.putString(PREF_GOOGLE_ID_TOKEN_KEY, value ?: EMPTY_VALUE_STRING)
}