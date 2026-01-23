package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.AuthApiService
import com.aryandi.paymentnfc.data.network.AuthApiServiceImpl
import com.aryandi.paymentnfc.data.repository.AuthRepositoryImpl
import com.aryandi.paymentnfc.domain.repository.AuthRepository
import com.aryandi.paymentnfc.domain.usecase.LoginUseCase
import com.aryandi.paymentnfc.domain.usecase.RegisterUseCase
import com.aryandi.paymentnfc.domain.usecase.ResendOtpUseCase
import com.aryandi.paymentnfc.domain.usecase.VerifyOtpUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authModule = module {
    // API Service
    single<AuthApiService> { AuthApiServiceImpl(get()) }
    
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    
    // Use Cases
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::VerifyOtpUseCase)
    factoryOf(::ResendOtpUseCase)
}
