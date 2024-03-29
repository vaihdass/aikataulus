package ru.vaihdass.aikataulus.presentation.base

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
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

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

fun Fragment.toastShort(msg: String) = toastMsg(Toast.LENGTH_SHORT, msg)
fun Fragment.toastLong(msg: String) = toastMsg(Toast.LENGTH_LONG, msg)
fun Fragment.toastShort(@StringRes stringRes: Int) = toastStringRes(Toast.LENGTH_SHORT, stringRes)
fun Fragment.toastLong(@StringRes stringRes: Int) = toastStringRes(Toast.LENGTH_LONG, stringRes)

private fun Fragment.toastStringRes(toastLength: Int, @StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, toastLength).show()
}
private fun Fragment.toastMsg(toastLength: Int, msg: String) {
    Toast.makeText(requireContext(), msg, toastLength).show()
}