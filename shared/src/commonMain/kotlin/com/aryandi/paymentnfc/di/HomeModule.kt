package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HomeApiService
import com.aryandi.paymentnfc.data.network.HomeApiServiceImpl
import com.aryandi.paymentnfc.data.repository.HomeRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import org.koin.dsl.module

val homeModule = module {
    single<HomeApiService> { HomeApiServiceImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    
    // Use Cases (Domain layer)
    factory { GetTransactionsUseCase(get()) }
    factory { GetCardsUseCase() }
}
