package ru.vaihdass.aikataulus.data.auth

import android.content.Context
import dagger.Module
import dagger.Provides
import net.openid.appauth.AuthorizationService
import javax.inject.Singleton

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthorizationService(ctx: Context) = AuthorizationService(ctx)
}