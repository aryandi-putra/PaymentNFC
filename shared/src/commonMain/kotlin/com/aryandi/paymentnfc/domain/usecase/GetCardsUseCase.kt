package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting all cards organized by category
 * Uses CardRepository to fetch data from local database
 */
class GetCardsUseCase(
    private val cardRepository: CardRepository
) {
    /**
     * Get cards grouped by category as a Flow for reactive updates
     * @param categories List of categories to group by
     * @return Flow of Map with categoryId as key and list of cards as value
     */
    fun observeCardsGroupedByCategory(categories: List<Category>): Flow<Map<String, List<Card>>> {
        return cardRepository.getCardsGroupedByCategory(categories)
    }
    
    /**
     * Get all cards as a Flow
     */
    fun observeAllCards(): Flow<List<Card>> {
        return cardRepository.getAllCards()
    }
    
    /**
     * Get cards by specific categoryId
     */
    fun observeCardsByCategoryId(categoryId: String): Flow<List<Card>> {
        return cardRepository.getCardsByCategoryId(categoryId)
    }
}
