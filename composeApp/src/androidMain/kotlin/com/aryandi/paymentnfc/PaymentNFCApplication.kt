package com.aryandi.paymentnfc

import android.app.Application
import android.content.pm.ApplicationInfo
import co.touchlab.kermit.Logger
import com.aryandi.paymentnfc.config.AppConfig
import com.aryandi.paymentnfc.config.ConfigHolder
import com.aryandi.paymentnfc.config.Environment
import com.aryandi.paymentnfc.config.kermitMinSeverity
import com.aryandi.paymentnfc.di.NetworkConfig
import com.aryandi.paymentnfc.di.initKoin
import com.aryandi.paymentnfc.logging.CrashHandler
import com.aryandi.paymentnfc.logging.KermitLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.module

class PaymentNFCApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        // 1. Initialize AppConfig
        ConfigHolder.config = AppConfig(
            isDebug = isDebuggable,
            enableNetworkLogging = isDebuggable,
            environment = if (isDebuggable) Environment.DEV else Environment.PROD
        )

        // 2. Initialize Kermit with configured severity
        initializeKermit()

        initKoin {
            // Enable Android-specific features
            androidLogger(Level.ERROR) // Log only errors in production
            androidContext(this@PaymentNFCApplication)

            // Platform overrides
            modules(
                module {
                    single { NetworkConfig(enableNetworkLogs = ConfigHolder.config.enableNetworkLogging) }
                }
            )
        }
        
        // Set up crash handler to log uncaught exceptions
        CrashHandler.setupDefaultExceptionHandler()
    }
    
    private fun initializeKermit() {
        val config = ConfigHolder.config
        
        // Set the global minimum severity for Kermit
        Logger.setMinSeverity(config.kermitMinSeverity())

        val logMessage = "PaymentNFC Application starting in ${config.environment} mode (isDebug=${config.isDebug})"
        
        if (config.isDebug) {
            KermitLogger.d(logMessage)
        } else {
            KermitLogger.i(logMessage)
        }
        
        KermitLogger.i("Kermit logging initialized with min severity: ${config.kermitMinSeverity()}")
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        KermitLogger.w("System is running low on memory")
    }
    
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        KermitLogger.d("Memory trimmed with level: $level")
    }
}
