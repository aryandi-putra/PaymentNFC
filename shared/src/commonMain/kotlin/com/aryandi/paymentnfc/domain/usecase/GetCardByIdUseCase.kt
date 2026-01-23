package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.repository.CardRepository

/**
 * Use case for getting a card by its ID
 */
class GetCardByIdUseCase(
    private val cardRepository: CardRepository
) {
    /**
     * Get a card by its ID
     * 
     * @param cardId The ID of the card to retrieve
     * @return Result containing the card or failure
     */
    suspend operator fun invoke(cardId: String): Result<Card?> {
        return try {
            val card = cardRepository.getCardById(cardId)
            Result.success(card)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
