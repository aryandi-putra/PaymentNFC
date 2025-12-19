package com.aryandi.paymentnfc.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        // Allow platform modules (loaded after shared modules) to override definitions.
        allowOverride(true)

        // Load shared modules first, then allow platforms to override (e.g. NetworkConfig).
        modules(sharedModule)
        config?.invoke(this)
    }
}

val sharedModule: List<Module> = listOf(
    loggingModule,
    networkModule,
    authModule,
    homeModule
)