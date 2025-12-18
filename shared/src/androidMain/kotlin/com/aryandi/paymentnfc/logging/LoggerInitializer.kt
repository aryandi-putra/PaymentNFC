package com.aryandi.paymentnfc.logging

/**
 * Android implementation of LoggerInitializer
 */
actual object LoggerInitializer {
    actual fun initialize(isDebuggable: Boolean) {
        // Android uses LogcatWriter which is already set up with Kermit
        val logMessage = "Android Logger initialized in ${
            if (isDebuggable) "DEBUG" else "RELEASE"
        } mode"
        
        if (isDebuggable) {
            KermitLogger.d(logMessage)
        } else {
            KermitLogger.i(logMessage)
        }
    }
}