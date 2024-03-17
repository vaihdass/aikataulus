package ru.vaihdass.aikataulus.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.vaihdass.aikataulus.presentation.utils.ViewModelFactory

@Module(
    includes = [
        PresentationModuleBinder::class,
        ViewModelModule::class,
    ],
)
interface PresentationModule {
    /* TODO: Add view models for screens
    @Provides
    @[IntoMap ViewModelKey(SomeViewModel::class)]
    fun provideWeatherMainViewModel(viewModel: SomeViewModel): ViewModel = viewModel*/
}