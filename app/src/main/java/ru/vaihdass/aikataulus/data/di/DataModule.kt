package ru.vaihdass.aikataulus.data.di

import dagger.Module
import ru.vaihdass.aikataulus.data.local.DatabaseModule
import ru.vaihdass.aikataulus.data.remote.NetworkModule

@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        DataModuleBinder::class,
    ]
)
class DataModule {}