package com.aryandi.paymentnfc.logging

/**
 * Platform-specific logger initializer
 */
expect object LoggerInitializer {
    fun initialize(isDebuggable: Boolean)
}