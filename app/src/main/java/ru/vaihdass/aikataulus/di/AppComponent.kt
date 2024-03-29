package ru.vaihdass.aikataulus.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.vaihdass.aikataulus.data.di.DataModule
import ru.vaihdass.aikataulus.domain.di.DomainModule
import ru.vaihdass.aikataulus.presentation.MainActivity
import ru.vaihdass.aikataulus.presentation.di.PresentationModule
import ru.vaihdass.aikataulus.presentation.screen.auth.AuthFragment
import ru.vaihdass.aikataulus.presentation.screen.greeting.GreetingFragment
import javax.inject.Singleton

@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        PresentationModule::class,
    ],
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideContext(ctx: Context): Builder
        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: GreetingFragment)
    fun inject(fragment: AuthFragment)
}