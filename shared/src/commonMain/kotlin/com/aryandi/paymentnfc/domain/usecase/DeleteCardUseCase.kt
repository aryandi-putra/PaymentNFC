package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.repository.CardRepository

/**
 * Use case for deleting a card from the database
 */
class DeleteCardUseCase(
    private val cardRepository: CardRepository
) {
    /**
     * Delete a card by its ID
     * 
     * @param cardId The ID of the card to delete
     * @return Result indicating success or failure
     */
    suspend operator fun invoke(cardId: String): Result<Unit> {
        return try {
            cardRepository.deleteCard(cardId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
