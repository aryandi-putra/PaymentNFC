package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Use case for getting all cards organized by category
 * Uses CardRepository to fetch data from local database
 */
class GetCardsUseCase(
    private val cardRepository: CardRepository
) {
    /**
     * Get cards grouped by category as a one-time result
     */
    suspend operator fun invoke(): Result<Map<CardCategory, List<Card>>> {
        return try {
            // Get cards grouped by category (returns empty lists if no cards)
            val cardsMap = cardRepository.getCardsGroupedByCategory().first()
            Result.success(cardsMap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get cards grouped by category as a Flow for reactive updates
     */
    fun observeCards(): Flow<Map<CardCategory, List<Card>>> {
        return cardRepository.getCardsGroupedByCategory()
    }
    
    /**
     * Get cards by specific category
     */
    fun observeCardsByCategory(category: CardCategory): Flow<List<Card>> {
        return cardRepository.getCardsByCategory(category)
    }
}
