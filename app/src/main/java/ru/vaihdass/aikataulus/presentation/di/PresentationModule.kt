package ru.vaihdass.aikataulus.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.vaihdass.aikataulus.presentation.screen.auth.AuthViewModel
import ru.vaihdass.aikataulus.presentation.screen.greeting.OrganizationViewModel
import ru.vaihdass.aikataulus.presentation.screen.main.MainViewModel

@Module(
    includes = [
        ViewModelModuleBinder::class,
    ],
)
class PresentationModule {
    @Provides
    @[IntoMap ViewModelKey(AuthViewModel::class)]
    fun provideAuthViewModel(viewModel: AuthViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(OrganizationViewModel::class)]
    fun provideGreetingViewModel(viewModel: OrganizationViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun provideMainViewModel(viewModel: MainViewModel): ViewModel = viewModel
}