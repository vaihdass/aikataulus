package ru.vaihdass.aikataulus.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch

fun <T> Flow<T>.catchWithHandler(
    exceptionHandlerDelegate: ExceptionHandlerDelegate,
    onError: FlowCollector<T>.(Throwable) -> Unit,
): Flow<T> = catch { e ->
    exceptionHandlerDelegate.handleException(e)
    onError(e)
}

inline fun <T, R> T.runCatching(
    exceptionHandlerDelegate: ExceptionHandlerDelegate,
    block: T.() -> R,
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(exceptionHandlerDelegate.handleException(e))
    }
}