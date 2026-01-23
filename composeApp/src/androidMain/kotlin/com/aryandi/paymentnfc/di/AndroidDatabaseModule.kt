package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific Koin module for database dependencies
 * Provides DatabaseDriverFactory with Android Context
 */
val androidDatabaseModule = module {
    single { DatabaseDriverFactory(androidContext()) }
}
