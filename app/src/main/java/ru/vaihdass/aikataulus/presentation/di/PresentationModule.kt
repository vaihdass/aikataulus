package ru.vaihdass.aikataulus.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.vaihdass.aikataulus.presentation.screen.auth.AuthViewModel
import ru.vaihdass.aikataulus.presentation.screen.greeting.GreetingViewModel

@Module(
    includes = [
        PresentationModuleBinder::class,
        ViewModelModule::class,
    ],
)
class PresentationModule {

    @Provides
    @[IntoMap ViewModelKey(GreetingViewModel::class)]
    fun provideGreetingViewModel(viewModel: GreetingViewModel): ViewModel = viewModel

    @Provides
    @[IntoMap ViewModelKey(AuthViewModel::class)]
    fun provideAuthViewModel(viewModel: AuthViewModel): ViewModel = viewModel
}