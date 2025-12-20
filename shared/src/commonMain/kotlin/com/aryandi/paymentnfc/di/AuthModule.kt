package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.AuthApiService
import com.aryandi.paymentnfc.data.network.AuthApiServiceImpl
import com.aryandi.paymentnfc.data.repository.AuthRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.AuthRepository
import com.aryandi.paymentnfc.domain.usecase.LoginUseCase
import com.aryandi.paymentnfc.presentation.viewmodel.LoginViewModel
import org.koin.dsl.module

val authModule = module {
    // API Service
    single<AuthApiService> { AuthApiServiceImpl(get()) }
    
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    
    // Use Cases
    factory { LoginUseCase(get()) }
    
    // ViewModels
    factory { LoginViewModel(get()) }
}
