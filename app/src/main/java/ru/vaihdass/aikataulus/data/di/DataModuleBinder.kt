package ru.vaihdass.aikataulus.data.di

import dagger.Binds
import dagger.Module
import ru.vaihdass.aikataulus.data.repository.TasksRepositoryImpl
import ru.vaihdass.aikataulus.domain.repository.TasksRepository

@Module
interface DataModuleBinder {
    @Binds
    fun bindTasksRepository_to_TasksRepositoryImpl(repositoryImpl: TasksRepositoryImpl): TasksRepository
}