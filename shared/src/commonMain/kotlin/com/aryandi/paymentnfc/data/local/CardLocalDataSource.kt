package com.aryandi.paymentnfc.data.local

import com.aryandi.paymentnfc.database.CardEntity
import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for Card operations
 * Abstracts SQLDelight database operations
 */
interface CardLocalDataSource {
    
    /**
     * Get all cards from the database
     */
    fun getAllCards(): Flow<List<CardEntity>>
    
    /**
     * Get cards by categoryId
     */
    fun getCardsByCategoryId(categoryId: String): Flow<List<CardEntity>>
    
    /**
     * Get a single card by ID
     */
    suspend fun getCardById(id: String): CardEntity?
    
    /**
     * Insert or update a card
     */
    suspend fun insertCard(card: CardEntity)
    
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
     * Count cards by categoryId
     */
    suspend fun countByCategoryId(categoryId: String): Long
    
    /**
     * Set a card as default payment (clears other defaults first)
     */
    suspend fun setCardAsDefault(cardId: String)
    
    /**
     * Get the current default card
     */
    suspend fun getDefaultCard(): CardEntity?
}
