package com.aryandi.paymentnfc.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}

val sharedModule: List<Module> = listOf(
    networkModule
)