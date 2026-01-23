package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.local.CardLocalDataSource
import com.aryandi.paymentnfc.data.mapper.CardMapper
import com.aryandi.paymentnfc.database.CardEntity
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

/**
 * Implementation of CardRepository using local data source
 */
class CardRepositoryImpl(
    private val localDataSource: CardLocalDataSource
) : CardRepository {
    
    override fun getAllCards(): Flow<List<Card>> {
        return localDataSource.getAllCards().map { entities ->
            entities.map { CardMapper.toDomain(it) }
        }
    }
    
    override fun getCardsGroupedByCategory(categories: List<Category>): Flow<Map<String, List<Card>>> {
        if (categories.isEmpty()) {
            return flowOf(emptyMap())
        }
        
        // Create flows for each category
        val categoryFlows = categories.map { category ->
            localDataSource.getCardsByCategoryId(category.id).map { entities ->
                category.id to entities.map { CardMapper.toDomain(it) }
            }
        }
        
        // Combine all flows into a single map
        return combine(categoryFlows) { results ->
            results.toMap()
        }
    }
    
    override fun getCardsByCategoryId(categoryId: String): Flow<List<Card>> {
        return localDataSource.getCardsByCategoryId(categoryId).map { entities ->
            entities.map { CardMapper.toDomain(it) }
        }
    }
    
    override suspend fun getCardById(id: String): Card? {
        return localDataSource.getCardById(id)?.let { CardMapper.toDomain(it) }
    }
    
    override suspend fun saveCard(card: Card) {
        localDataSource.insertCard(CardMapper.toEntity(card))
    }
    
    override suspend fun saveCards(cards: List<Card>) {
        cards.forEach { card ->
            localDataSource.insertCard(CardMapper.toEntity(card))
        }
    }
    
    override suspend fun deleteCard(id: String) {
        localDataSource.deleteCard(id)
    }
    
    override suspend fun deleteCardsByCategoryId(categoryId: String) {
        localDataSource.deleteCardsByCategoryId(categoryId)
    }
    
    override suspend fun deleteAllCards() {
        localDataSource.deleteAllCards()
    }
    
    override suspend fun setCardAsDefault(cardId: String) {
        localDataSource.setCardAsDefault(cardId)
    }
    
    override suspend fun getDefaultCard(): Card? {
        return localDataSource.getDefaultCard()?.let { CardMapper.toDomain(it) }
    }
}
