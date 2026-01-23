package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HomeApiService
import com.aryandi.paymentnfc.data.network.HomeApiServiceImpl
import com.aryandi.paymentnfc.data.repository.HomeRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import com.aryandi.paymentnfc.domain.usecase.AddCardUseCase
import com.aryandi.paymentnfc.domain.usecase.AddCategoryUseCase
import com.aryandi.paymentnfc.domain.usecase.DeleteCategoryUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCardTransactionsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCategoriesUseCase
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import org.koin.dsl.module

val homeModule = module {
    single<HomeApiService> { HomeApiServiceImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    
    // Card Use Cases
    factory { GetTransactionsUseCase(get()) }
    factory { GetCardsUseCase(get()) }
    factory { AddCardUseCase(get()) }
    factory { GetCardTransactionsUseCase() }
    
    // Category Use Cases
    factory { GetCategoriesUseCase(get()) }
    factory { AddCategoryUseCase(get()) }
    factory { DeleteCategoryUseCase(get(), get()) }
}
