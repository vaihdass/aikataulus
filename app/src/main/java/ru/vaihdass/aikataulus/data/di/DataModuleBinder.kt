package ru.vaihdass.aikataulus.data.di

import dagger.Binds
import dagger.Module
import ru.vaihdass.aikataulus.data.repository.AikataulusRepositoryImpl
import ru.vaihdass.aikataulus.data.repository.EventsRepositoryImpl
import ru.vaihdass.aikataulus.data.repository.TasksRepositoryImpl
import ru.vaihdass.aikataulus.domain.repository.AikataulusRepository
import ru.vaihdass.aikataulus.domain.repository.EventsRepository
import ru.vaihdass.aikataulus.domain.repository.TasksRepository

@Module
interface DataModuleBinder {
    @Binds
    fun bindTasksRepository(repositoryImpl: TasksRepositoryImpl): TasksRepository

    @Binds
    fun bindEventsRepository(repositoryImpl: EventsRepositoryImpl): EventsRepository

    @Binds
    fun bindAikataulusRepository(repositoryImpl: AikataulusRepositoryImpl): AikataulusRepository
}