package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.local.CardLocalDataSource
import com.aryandi.paymentnfc.data.local.CardLocalDataSourceImpl
import com.aryandi.paymentnfc.data.local.CategoryLocalDataSource
import com.aryandi.paymentnfc.data.local.CategoryLocalDataSourceImpl
import com.aryandi.paymentnfc.data.local.DatabaseDriverFactory
import com.aryandi.paymentnfc.data.repository.CardRepositoryImpl
import com.aryandi.paymentnfc.data.repository.CategoryRepositoryImpl
import com.aryandi.paymentnfc.database.PaymentNFCDatabase
import com.aryandi.paymentnfc.domain.repository.CardRepository
import com.aryandi.paymentnfc.domain.repository.CategoryRepository
import org.koin.dsl.module

/**
 * Koin module for database dependencies
 * Note: DatabaseDriverFactory must be provided by each platform (Android/iOS)
 */
val databaseModule = module {
    // Database instance - expects DatabaseDriverFactory from platform module
    single {
        PaymentNFCDatabase(get<DatabaseDriverFactory>().createDriver())
    }
    
    // Local data sources
    single<CardLocalDataSource> { CardLocalDataSourceImpl(get()) }
    single<CategoryLocalDataSource> { CategoryLocalDataSourceImpl(get()) }
    
    // Repositories
    single<CardRepository> { CardRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
}
