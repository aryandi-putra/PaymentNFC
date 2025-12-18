package com.aryandi.paymentnfc.logging

/**
 * iOS implementation of LoggerInitializer
 */
actual object LoggerInitializer {
    actual fun initialize(isDebuggable: Boolean) {
        // Initialize iOS-specific logging configuration
        val logMessage = "iOS Logger initialized in ${
            if (isDebuggable) "DEBUG" else "RELEASE"
        } mode"
        
        if (isDebuggable) {
            KermitLogger.d(logMessage)
        } else {
            KermitLogger.i(logMessage)
        }
        
        // Log system information
        KermitLogger.i("iOS Logging System initialized")
    }
}