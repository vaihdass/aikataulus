package ru.vaihdass.aikataulus.presentation.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {
    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragment = this@BaseFragment, block)
    }
}