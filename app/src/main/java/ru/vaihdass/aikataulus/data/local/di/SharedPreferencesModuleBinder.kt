package ru.vaihdass.aikataulus.data.local.di

import dagger.Binds
import dagger.Module
import ru.vaihdass.aikataulus.data.local.pref.AikataulusSharedPreferencesManager
import ru.vaihdass.aikataulus.data.local.pref.SharedPreferencesManager

@Module
interface SharedPreferencesModuleBinder {
    @Binds
    fun bindSharedPreferencesManager_to_SharedPreferencesManagerImpl(prefsManagerImpl: AikataulusSharedPreferencesManager): SharedPreferencesManager
}