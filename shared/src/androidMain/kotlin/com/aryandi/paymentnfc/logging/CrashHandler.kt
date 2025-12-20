package com.aryandi.paymentnfc.logging

import kotlin.system.exitProcess

/**
 * Platform-specific crash handler implementation for Android
 */
actual object CrashHandler {
    actual fun handleCrash(throwable: Throwable) {
        val stackTrace = throwable.stackTraceToString()
        KermitLogger.logCrash(
            errorMessage = throwable.message ?: "Unknown error",
            stackTrace = stackTrace,
            throwable = throwable
        )
    }
    
    /**
     * Sets up a default exception handler for the current thread
     */
    fun setupDefaultExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleCrash(throwable)
            
            // Call the original handler if it exists
            defaultHandler?.uncaughtException(thread, throwable)
            
            // Exit the app
            exitProcess(1)
        }
    }
}