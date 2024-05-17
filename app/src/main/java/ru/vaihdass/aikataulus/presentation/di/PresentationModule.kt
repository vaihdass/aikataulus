package ru.vaihdass.aikataulus.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.vaihdass.aikataulus.presentation.screen.auth.AuthViewModel
import ru.vaihdass.aikataulus.presentation.screen.greeting.OrganizationViewModel
import ru.vaihdass.aikataulus.presentation.screen.main.MainViewModel
import ru.vaihdass.aikataulus.presentation.screen.schedule.ScheduleViewModel
import ru.vaihdass.aikataulus.presentation.screen.settings.SettingsViewModel
import ru.vaihdass.aikataulus.presentation.screen.taskcreate.TaskCreateViewModel
import ru.vaihdass.aikataulus.presentation.screen.tasks.TasksViewModel
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModuleBinder::class,
    ],
)
class PresentationModule {
    @Provides
    @Singleton
    @TaskChangedFlow
    fun provideTaskChangedFlow(): MutableSharedFlow<Unit> {
        return MutableSharedFlow<Unit>(0)
    }

    @Provides
    @[IntoMap ViewModelKey(AuthViewModel::class)]
    fun provideAuthViewModel(viewModel: AuthViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(OrganizationViewModel::class)]
    fun provideGreetingViewModel(viewModel: OrganizationViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun provideMainViewModel(viewModel: MainViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(SettingsViewModel::class)]
    fun provideSettingsViewModel(viewModel: SettingsViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(TaskCreateViewModel::class)]
    fun provideTaskCreateViewModel(viewModel: TaskCreateViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(TasksViewModel::class)]
    fun provideTasksViewModel(viewModel: TasksViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(ScheduleViewModel::class)]
    fun provideScheduleViewModel(viewModel: ScheduleViewModel): ViewModel = viewModel
}