package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.presentation.viewmodel.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    factory { (userId: String) -> HomeViewModel(userId = userId) }
}
