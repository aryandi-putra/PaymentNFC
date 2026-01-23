package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.domain.usecase.*
import org.koin.dsl.module

/**
 * DI module for Card and Category related dependencies
 */
val cardModule = module {
    // Card Use Cases
    factory { GetCardsUseCase(get()) }
    factory { AddCardUseCase(get()) }
    factory { GetCardByIdUseCase(get()) }
    factory { SetDefaultCardUseCase(get()) }
    factory { DeleteCardUseCase(get()) }
    
    // Category Use Cases
    factory { GetCategoriesUseCase(get()) }
    factory { AddCategoryUseCase(get()) }
    factory { DeleteCategoryUseCase(get(), get()) }
}
