package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.UseCase
import com.aryandi.paymentnfc.domain.model.Card
import kotlinx.coroutines.delay

/**
 * Use case for adding a new card
 * Currently simulates a successful API/DB call
 */
open class AddCardUseCase : UseCase<Card, Unit>() {
    
    override suspend fun invoke(params: Card): Result<Unit> {
        return try {
            // Simulate network/db delay
            delay(1000)
            // In a real app, this would save to repository
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
