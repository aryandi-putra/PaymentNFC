package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.TransactionApiService
import com.aryandi.paymentnfc.data.network.TransactionApiServiceImpl
import com.aryandi.paymentnfc.data.repository.TransactionRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.TransactionRepository
import com.aryandi.paymentnfc.domain.usecase.GetCardTransactionsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import org.koin.dsl.module

/**
 * DI module for Transaction related dependencies
 */
val transactionModule = module {
    single<TransactionApiService> { TransactionApiServiceImpl(get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    
    // Transaction Use Cases
    factory { GetTransactionsUseCase(get()) }
    factory { GetCardTransactionsUseCase(get()) }
}
