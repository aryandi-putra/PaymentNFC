package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.repository.CardRepository

/**
 * Use case for adding a new card
 * Saves the card to the local database via CardRepository
 */
class AddCardUseCase(
    private val cardRepository: CardRepository
) {
    
    suspend operator fun invoke(card: Card): Result<Unit> {
        return try {
            cardRepository.saveCard(card)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
