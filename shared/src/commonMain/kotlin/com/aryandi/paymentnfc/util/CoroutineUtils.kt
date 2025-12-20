package com.aryandi.paymentnfc.util

import com.aryandi.paymentnfc.logging.CrashHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

suspend fun safeApiCall(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        CrashHandler.handleCrash(e)
        throw e
    }
}

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    CrashHandler.handleCrash(throwable)
}

fun CoroutineScope.launchSafe(
    context: kotlin.coroutines.CoroutineContext = coroutineExceptionHandler,
    block: suspend () -> Unit
): Job {
    return launch(context) {
        safeApiCall { block() }
    }
}
