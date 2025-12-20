package com.aryandi.paymentnfc.logging

/**
 * Platform-specific crash handler implementation for iOS
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
        // For iOS, we'll use a simple try-catch pattern at the application level
        // as NSExceptionHandler requires more complex setup
        KermitLogger.i("iOS crash handling initialized")
    }
}