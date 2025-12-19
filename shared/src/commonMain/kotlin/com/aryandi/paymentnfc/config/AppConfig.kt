package com.aryandi.paymentnfc.config

import co.touchlab.kermit.Severity

data class AppConfig(
    val isDebug: Boolean,
    val enableNetworkLogging: Boolean,
    val environment: Environment
)

enum class Environment {
    DEV, STAGING, PROD
}

/**
 * Returns the minimum Kermit severity that should be enabled for this app configuration.
 * Use this from platform initialization to set the logger's min severity.
 */
fun AppConfig.kermitMinSeverity(): Severity =
    if (isDebug) Severity.Debug else Severity.Warn

object ConfigHolder {
    lateinit var config: AppConfig
}
