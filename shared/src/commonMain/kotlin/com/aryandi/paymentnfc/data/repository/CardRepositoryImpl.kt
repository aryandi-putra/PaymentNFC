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
        // Check if database is empty and seed initial data
        val count = localDataSource.countByCategory(CardCategory.RETAIL_SHOPPING.name) +
                localDataSource.countByCategory(CardCategory.MEMBER_CARD.name) +
                localDataSource.countByCategory(CardCategory.ELECTRONIC_MONEY.name)
        
        if (count == 0L) {
            seedInitialData()
        }
    }
    
    private suspend fun seedInitialData() {
        val initialCards = listOf(
            // Debit/Credit Cards
            Card(
                id = "1",
                bankName = "AriaBank",
                cardType = CardTypeModel.VISA,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 1234",
                maskedNumber = "$125.50",
                colorHex = "#FF6B4A"
            ),
            Card(
                id = "2",
                bankName = "AriaBank",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 5678",
                maskedNumber = "$89.99",
                colorHex = "#FFD93D"
            ),
            Card(
                id = "3",
                bankName = "WeBank",
                cardType = CardTypeModel.VISA,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 9012",
                maskedNumber = "$1,450.00",
                cardHolder = "Alexander Parra",
                colorHex = "#A4B494"
            ),
            // Member Cards
            Card(
                id = "4",
                bankName = "Alfagift",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 3456",
                maskedNumber = "350 pts",
                colorHex = "#8B6F47"
            ),
            Card(
                id = "5",
                bankName = "IKEA",
                cardType = CardTypeModel.VISA,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 7890",
                maskedNumber = "850 pts",
                colorHex = "#FFD93D"
            ),
            Card(
                id = "6",
                bankName = "Starbucks",
                cardType = CardTypeModel.VISA,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 2468",
                maskedNumber = "$45.50",
                cardHolder = "Alexander Parra",
                colorHex = "#2D5F3F"
            ),
            // Electronic Money Cards
            Card(
                id = "7",
                bankName = "BNI Tapcash",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 1357",
                maskedNumber = "$55.00",
                colorHex = "#FF6B4A"
            ),
            Card(
                id = "8",
                bankName = "Mandiri E-Money",
                cardType = CardTypeModel.VISA,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 2468",
                maskedNumber = "$120.75",
                colorHex = "#1E3A5F"
            ),
            Card(
                id = "9",
                bankName = "Flazz BCA",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 9753",
                maskedNumber = "$200.00",
                cardHolder = "Alexander Parra",
                colorHex = "#4A90E2"
            )
        )
        
        saveCards(initialCards)
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
