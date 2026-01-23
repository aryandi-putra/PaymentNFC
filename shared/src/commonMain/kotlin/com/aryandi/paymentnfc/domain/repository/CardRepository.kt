package com.aryandi.paymentnfc.domain.repository

import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
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
     * Get cards grouped by category
     */
    fun getCardsGroupedByCategory(): Flow<Map<CardCategory, List<Card>>>
    
    /**
     * Get cards by category
     */
    fun getCardsByCategory(category: CardCategory): Flow<List<Card>>
    
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
    suspend fun deleteCardsByCategory(category: CardCategory)
    
    /**
     * Delete all cards
     */
    suspend fun deleteAllCards()
    
    /**
     * Seed initial data (if database is empty)
     */
    suspend fun seedInitialDataIfNeeded()
}
