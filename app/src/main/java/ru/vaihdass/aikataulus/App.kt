package ru.vaihdass.aikataulus

import android.app.Application
import ru.vaihdass.aikataulus.di.AppComponent
import ru.vaihdass.aikataulus.di.DaggerAppComponent
import timber.log.Timber


class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        appComponent = DaggerAppComponent.builder()
            .provideContext(ctx = this)
            .build()
    }
}