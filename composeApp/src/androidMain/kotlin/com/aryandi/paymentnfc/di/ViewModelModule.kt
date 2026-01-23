package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.presentation.viewmodel.AddCardViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.CardsViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.HomeV2ViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.HomeViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.LoginViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.OtpViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for Android ViewModels
 * Separated from shared module as ViewModels are platform-specific
 */
val viewModelModule = module {
    // Auth ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::OtpViewModel)
    
    // Home ViewModels
    viewModel { (userId: String) -> 
        HomeViewModel(userId = userId, getTransactionsUseCase = get()) 
    }
    viewModelOf(::HomeV2ViewModel)
    
    // Cards ViewModels
    viewModelOf(::CardsViewModel)
    viewModelOf(::AddCardViewModel)
}
