package com.aryandi.paymentnfc.domain.repository

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Card operations
 * Defines the contract for card data management
 */
interface CardRepository {
    
    /**
     * Get all cards as a Flow
     */
    fun getAllCards(): Flow<List<Card>>
    
    /**
     * Get cards grouped by category (dynamic categories from database)
     */
    fun getCardsGroupedByCategory(categories: List<Category>): Flow<Map<String, List<Card>>>
    
    /**
     * Get cards by categoryId
     */
    fun getCardsByCategoryId(categoryId: String): Flow<List<Card>>
    
    /**
     * Get a single card by ID
     */
    suspend fun getCardById(id: String): Card?
    
    /**
     * Insert or update a card
     */
    suspend fun saveCard(card: Card)
    
    /**
     * Insert multiple cards
     */
    suspend fun saveCards(cards: List<Card>)
    
    /**
     * Delete a card by ID
     */
    suspend fun deleteCard(id: String)
    
    /**
     * Delete all cards in a category
     */
    suspend fun deleteCardsByCategoryId(categoryId: String)
    
    /**
     * Delete all cards
     */
    suspend fun deleteAllCards()
    
    /**
     * Set a card as the default payment method
     */
    suspend fun setCardAsDefault(cardId: String)
    
    /**
     * Get the current default card
     */
    suspend fun getDefaultCard(): Card?
}
