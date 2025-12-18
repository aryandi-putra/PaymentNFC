package com.aryandi.paymentnfc.logging

/**
 * Common interface for crash handling across platforms
 */
expect object CrashHandler {
    fun handleCrash(throwable: Throwable)
}