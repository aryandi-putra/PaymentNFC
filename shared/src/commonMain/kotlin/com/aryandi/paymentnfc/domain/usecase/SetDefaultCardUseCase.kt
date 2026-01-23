package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.repository.CardRepository

/**
 * Use case for setting a card as the default payment method
 */
class SetDefaultCardUseCase(
    private val cardRepository: CardRepository
) {
    /**
     * Set the specified card as the default payment method
     * This will clear any existing default and set the new one
     * 
     * @param cardId The ID of the card to set as default
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(cardId: String): Result<Unit> {
        return try {
            cardRepository.setCardAsDefault(cardId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
