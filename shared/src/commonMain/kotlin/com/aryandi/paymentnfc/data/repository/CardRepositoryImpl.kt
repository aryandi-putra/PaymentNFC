package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.local.CardLocalDataSource
import com.aryandi.paymentnfc.database.CardEntity
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Implementation of CardRepository using local data source
 */
class CardRepositoryImpl(
    private val localDataSource: CardLocalDataSource
) : CardRepository {
    
    override fun getAllCards(): Flow<List<Card>> {
        return localDataSource.getAllCards().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getCardsGroupedByCategory(): Flow<Map<CardCategory, List<Card>>> {
        return combine(
            localDataSource.getCardsByCategory(CardCategory.RETAIL_SHOPPING.name),
            localDataSource.getCardsByCategory(CardCategory.MEMBER_CARD.name),
            localDataSource.getCardsByCategory(CardCategory.ELECTRONIC_MONEY.name)
        ) { retail, member, eMoney ->
            mapOf(
                CardCategory.RETAIL_SHOPPING to retail.map { it.toDomainModel() },
                CardCategory.MEMBER_CARD to member.map { it.toDomainModel() },
                CardCategory.ELECTRONIC_MONEY to eMoney.map { it.toDomainModel() }
            )
        }
    }
    
    override fun getCardsByCategory(category: CardCategory): Flow<List<Card>> {
        return localDataSource.getCardsByCategory(category.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getCardById(id: String): Card? {
        return localDataSource.getCardById(id)?.toDomainModel()
    }
    
    override suspend fun saveCard(card: Card) {
        localDataSource.insertCard(card.toEntity())
    }
    
    override suspend fun saveCards(cards: List<Card>) {
        cards.forEach { card ->
            localDataSource.insertCard(card.toEntity())
        }
    }
    
    override suspend fun deleteCard(id: String) {
        localDataSource.deleteCard(id)
    }
    
    override suspend fun deleteCardsByCategory(category: CardCategory) {
        localDataSource.deleteCardsByCategory(category.name)
    }
    
    override suspend fun deleteAllCards() {
        localDataSource.deleteAllCards()
    }
    
    override suspend fun seedInitialDataIfNeeded() {
        // No-op: Disabled auto-seeding for testing empty state
        // Uncomment the code below to re-enable seeding with dummy data
        /*
        val count = localDataSource.countByCategory(CardCategory.RETAIL_SHOPPING.name) +
                localDataSource.countByCategory(CardCategory.MEMBER_CARD.name) +
                localDataSource.countByCategory(CardCategory.ELECTRONIC_MONEY.name)
        
        if (count == 0L) {
            seedInitialData()
        }
        */
    }
    
    // Extension functions for mapping
    private fun CardEntity.toDomainModel(): Card {
        return Card(
            id = id,
            bankName = bankName,
            cardType = CardTypeModel.valueOf(cardType),
            cardNumber = cardNumber,
            maskedNumber = maskedNumber,
            cardHolder = cardHolder,
            category = CardCategory.valueOf(category),
            colorHex = colorHex
        )
    }
    
    private fun Card.toEntity(): CardEntity {
        return CardEntity(
            id = id,
            bankName = bankName,
            cardType = cardType.name,
            cardNumber = cardNumber,
            maskedNumber = maskedNumber,
            cardHolder = cardHolder,
            category = category.name,
            colorHex = colorHex
        )
    }
}
