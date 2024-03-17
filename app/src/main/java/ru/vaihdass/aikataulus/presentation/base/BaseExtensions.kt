package ru.vaihdass.aikataulus.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.vaihdass.aikataulus.App
import ru.vaihdass.aikataulus.di.AppComponent

inline fun <T> Flow<T>.observe(fragment: Fragment, crossinline block: (T) -> Unit): Job {
    val lifecycleOwner = fragment.viewLifecycleOwner
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collect { data -> block(data) }
        }
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }