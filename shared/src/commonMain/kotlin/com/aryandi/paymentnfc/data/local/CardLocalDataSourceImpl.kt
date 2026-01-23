package com.aryandi.paymentnfc.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aryandi.paymentnfc.database.CardEntity
import com.aryandi.paymentnfc.database.PaymentNFCDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Implementation of CardLocalDataSource using SQLDelight
 */
class CardLocalDataSourceImpl(
    database: PaymentNFCDatabase
) : CardLocalDataSource {
    
    private val queries = database.cardQueries
    
    override fun getAllCards(): Flow<List<CardEntity>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }
    
    override fun getCardsByCategory(category: String): Flow<List<CardEntity>> {
        return queries.selectByCategory(category).asFlow().mapToList(Dispatchers.IO)
    }
    
    override suspend fun getCardById(id: String): CardEntity? = withContext(Dispatchers.IO) {
        queries.selectById(id).executeAsOneOrNull()
    }
    
    override suspend fun insertCard(card: CardEntity) = withContext(Dispatchers.IO) {
        queries.insertCard(
            id = card.id,
            bankName = card.bankName,
            cardType = card.cardType,
            cardNumber = card.cardNumber,
            maskedNumber = card.maskedNumber,
            cardHolder = card.cardHolder,
            category = card.category,
            colorHex = card.colorHex
        )
    }
    
    override suspend fun deleteCard(id: String) = withContext(Dispatchers.IO) {
        queries.deleteById(id)
    }
    
    override suspend fun deleteCardsByCategory(category: String) = withContext(Dispatchers.IO) {
        queries.deleteByCategory(category)
    }
    
    override suspend fun deleteAllCards() = withContext(Dispatchers.IO) {
        queries.deleteAll()
    }
    
    override suspend fun countByCategory(category: String): Long = withContext(Dispatchers.IO) {
        queries.countByCategory(category).executeAsOne()
    }
}
