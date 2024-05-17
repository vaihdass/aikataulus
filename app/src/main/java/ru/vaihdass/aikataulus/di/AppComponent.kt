package ru.vaihdass.aikataulus.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.vaihdass.aikataulus.data.di.DataModule
import ru.vaihdass.aikataulus.domain.di.DomainModule
import ru.vaihdass.aikataulus.presentation.MainActivity
import ru.vaihdass.aikataulus.presentation.di.PresentationModule
import ru.vaihdass.aikataulus.presentation.screen.auth.AuthFragment
import ru.vaihdass.aikataulus.presentation.screen.event.EventFragment
import ru.vaihdass.aikataulus.presentation.screen.greeting.CoursesAssistedViewModel
import ru.vaihdass.aikataulus.presentation.screen.greeting.CoursesFragment
import ru.vaihdass.aikataulus.presentation.screen.greeting.OrganizationFragment
import ru.vaihdass.aikataulus.presentation.screen.main.MainFragment
import ru.vaihdass.aikataulus.presentation.screen.schedule.ScheduleFragment
import ru.vaihdass.aikataulus.presentation.screen.settings.SettingsFragment
import ru.vaihdass.aikataulus.presentation.screen.task.TaskAssistedViewModel
import ru.vaihdass.aikataulus.presentation.screen.task.TaskFragment
import ru.vaihdass.aikataulus.presentation.screen.taskcreate.TaskCreateFragment
import ru.vaihdass.aikataulus.presentation.screen.taskedit.TaskEditAssistedViewModel
import ru.vaihdass.aikataulus.presentation.screen.taskedit.TaskEditFragment
import ru.vaihdass.aikataulus.presentation.screen.tasks.TasksFragment
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
    fun inject(fragment: AuthFragment)
    fun inject(fragment: OrganizationFragment)
    fun inject(fragment: CoursesFragment)
    fun coursesAssistedViewModel(): CoursesAssistedViewModel.Factory
    fun inject(fragment: MainFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: EventFragment)
    fun inject(fragment: TaskFragment)
    fun taskAssistedViewModel(): TaskAssistedViewModel.Factory
    fun inject(fragment: TaskEditFragment)
    fun taskEditAssistedViewModel(): TaskEditAssistedViewModel.Factory
    fun inject(fragment: TaskCreateFragment)
    fun inject(fragment: TasksFragment)
    fun inject(fragment: ScheduleFragment)
}