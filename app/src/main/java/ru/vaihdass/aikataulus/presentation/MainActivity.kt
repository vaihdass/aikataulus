package ru.vaihdass.aikataulus.presentation

import android.os.Bundle
import android.os.PersistableBundle
import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.presentation.base.BaseActivity
import ru.vaihdass.aikataulus.presentation.base.appComponent

class MainActivity : BaseActivity(R.layout.activity_main) {
    override val fragmentContainerId: Int = R.id.main_activity_container

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        appComponent.inject(activity = this)
        setContentView(R.layout.activity_main)
    }
}