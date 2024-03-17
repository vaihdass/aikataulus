package ru.vaihdass.aikataulus.presentation.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.vaihdass.aikataulus.presentation.utils.ViewModelFactory

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory_to_ViewModelProviderFactory(impl: ViewModelFactory): ViewModelProvider.Factory
}