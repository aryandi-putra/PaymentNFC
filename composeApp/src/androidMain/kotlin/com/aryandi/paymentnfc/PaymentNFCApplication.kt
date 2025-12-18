package com.aryandi.paymentnfc

import android.app.Application
import android.content.pm.ApplicationInfo
import com.aryandi.paymentnfc.di.NetworkConfig
import com.aryandi.paymentnfc.di.initKoin
import com.aryandi.paymentnfc.logging.CrashHandler
import com.aryandi.paymentnfc.logging.KermitLogger
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class PaymentNFCApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        initializeKermit(isDebuggable)

        initKoin {
            // Enable Android-specific features
            androidLogger(Level.ERROR) // Log only errors in production
            androidContext(this@PaymentNFCApplication)

            // Platform overrides
            modules(
                module {
                    single { NetworkConfig(enableNetworkLogs = isDebuggable) }
                }
            )
        }
        
        // Set up crash handler to log uncaught exceptions
        CrashHandler.setupDefaultExceptionHandler()
    }
    
    private fun initializeKermit(isDebuggable: Boolean) {
        val logMessage = "PaymentNFC Application starting in ${
            if (isDebuggable) "DEBUG" else "RELEASE"
        } mode"
        
        if (isDebuggable) {
            KermitLogger.d(logMessage)
        } else {
            KermitLogger.i(logMessage)
        }
        
        // Log initialization time
        KermitLogger.i("Kermit logging initialized successfully")
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
