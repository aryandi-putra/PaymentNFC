package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.NoParamUseCase
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.model.CardTypeModel

/**
 * Use case for getting all cards organized by category
 * Currently returns dummy/mock data
 * TODO: Replace with repository implementation when API is ready
 */
open class GetCardsUseCase : NoParamUseCase<Map<CardCategory, List<Card>>>() {
    
    override suspend fun invoke(): Result<Map<CardCategory, List<Card>>> {
        return try {
            val cards = getDummyCards()
            Result.success(cards)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun getDummyCards(): Map<CardCategory, List<Card>> {
        val retailCards = listOf(
            Card(
                id = "1",
                bankName = "AriaBank",
                cardType = CardTypeModel.VISA,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 1234",
                maskedNumber = "$125.50",
                colorHex = "#FF6B4A" // Orange
            ),
            Card(
                id = "2",
                bankName = "AriaBank",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 5678",
                maskedNumber = "$89.99",
                colorHex = "#FFD93D" // Yellow
            ),
            Card(
                id = "3",
                bankName = "WeBank",
                cardType = CardTypeModel.VISA,
                category = CardCategory.RETAIL_SHOPPING,
                cardNumber = "**** **** **** 9012",
                maskedNumber = "$1,450.00",
                cardHolder = "Alexander Parra",
                colorHex = "#A4B494" // Olive
            )
        )
        
        val memberCards = listOf(
            Card(
                id = "4",
                bankName = "Alfagift",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 3456",
                maskedNumber = "350 pts",
                colorHex = "#8B6F47" // Brown
            ),
            Card(
                id = "5",
                bankName = "IKEA",
                cardType = CardTypeModel.VISA,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 7890",
                maskedNumber = "850 pts",
                colorHex = "#FFD93D" // Yellow
            ),
            Card(
                id = "6",
                bankName = "Starbucks",
                cardType = CardTypeModel.VISA,
                category = CardCategory.MEMBER_CARD,
                cardNumber = "**** **** **** 2468",
                maskedNumber = "$45.50",
                cardHolder = "Alexander Parra",
                colorHex = "#2D5F3F" // Dark Green
            )
        )
        
        val eMoneyCards = listOf(
            Card(
                id = "7",
                bankName = "BNI Tapcash",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 1357",
                maskedNumber = "$55.00",
                colorHex = "#FF6B4A" // Orange
            ),
            Card(
                id = "8",
                bankName = "Mandiri E-Money",
                cardType = CardTypeModel.VISA,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 2468",
                maskedNumber = "$120.75",
                colorHex = "#1E3A5F" // Navy
            ),
            Card(
                id = "9",
                bankName = "Flazz BCA",
                cardType = CardTypeModel.MASTERCARD,
                category = CardCategory.ELECTRONIC_MONEY,
                cardNumber = "**** **** **** 9753",
                maskedNumber = "$200.00",
                cardHolder = "Alexander Parra",
                colorHex = "#4A90E2" // Blue
            )
        )
        
        return mapOf(
            CardCategory.RETAIL_SHOPPING to retailCards,
            CardCategory.MEMBER_CARD to memberCards,
            CardCategory.ELECTRONIC_MONEY to eMoneyCards
        )
    }
}
