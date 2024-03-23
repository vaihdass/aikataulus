package ru.vaihdass.aikataulus.data.local.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.vaihdass.aikataulus.base.Constants

@Module(
    includes = [
        SharedPreferencesModuleBinder::class,
    ]
)
class SharedPreferencesModule {
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}