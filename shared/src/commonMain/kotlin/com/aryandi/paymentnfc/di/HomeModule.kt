package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HomeApiService
import com.aryandi.paymentnfc.data.repository.HomeRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import com.aryandi.paymentnfc.presentation.viewmodel.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    single { HomeApiService(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    factory { (userId: String) -> HomeViewModel(userId = userId, homeRepository = get()) }
}
