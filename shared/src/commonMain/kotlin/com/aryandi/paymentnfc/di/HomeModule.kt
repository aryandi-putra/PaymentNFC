package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HomeApiService
import com.aryandi.paymentnfc.data.network.HomeApiServiceImpl
import com.aryandi.paymentnfc.data.repository.HomeRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import com.aryandi.paymentnfc.presentation.viewmodel.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeApiService> { HomeApiServiceImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    
    // Use Cases
    factory { GetTransactionsUseCase(get()) }
    
    // ViewModels
    factory { (userId: String) -> HomeViewModel(userId = userId, getTransactionsUseCase = get()) }
}
