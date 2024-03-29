package ru.vaihdass.aikataulus.data.auth.model

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)