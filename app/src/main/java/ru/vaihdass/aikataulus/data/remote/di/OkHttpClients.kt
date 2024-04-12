package ru.vaihdass.aikataulus.data.remote.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AikataulusClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TasksClient
