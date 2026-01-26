package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.local.DatabaseDriverFactory
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * iOS-specific Koin module for database dependencies
 * Provides DatabaseDriverFactory for iOS
 */
val iosDatabaseModule = module {
    single { DatabaseDriverFactory() }
}

/**
 * Helper function to initialize Koin from iOS
 * Call this from Swift AppDelegate or SwiftUI App
 */
fun initKoinIOS() {
    startKoin {
        modules(
            iosDatabaseModule,
            *sharedModule.toTypedArray()
        )
    }
}
