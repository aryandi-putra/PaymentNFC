package com.aryandi.paymentnfc.domain.model

/**
 * Domain model for Card
 * Represents a payment card (debit, credit, member, e-money)
 */
data class Card(
    val id: String,
    val bankName: String,
    val cardType: CardTypeModel,
    val cardNumber: String = "**** **** **** ****",
    val maskedNumber: String = "$•••••",
    val cardHolder: String = "",
    val category: CardCategory,
    val colorHex: String = "#FF6B4A" // Default orange color
)

enum class CardTypeModel {
    VISA,
    MASTERCARD
}

enum class CardCategory {
    RETAIL_SHOPPING,
    MEMBER_CARD,
    ELECTRONIC_MONEY
}
