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
    val categoryId: String, // References Category.id
    val colorHex: String = "#FF6B4A", // Default orange color
    val isDefault: Boolean = false
)

enum class CardTypeModel {
    VISA,
    MASTERCARD
}
