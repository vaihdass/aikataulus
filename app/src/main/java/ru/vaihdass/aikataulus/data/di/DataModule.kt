package ru.vaihdass.aikataulus.data.di

import dagger.Module
import ru.vaihdass.aikataulus.data.local.di.DatabaseModule
import ru.vaihdass.aikataulus.data.local.di.SharedPreferencesModule
import ru.vaihdass.aikataulus.data.remote.NetworkModule

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        SharedPreferencesModule::class,
        DataModuleBinder::class,
    ]
)
class DataModule {}