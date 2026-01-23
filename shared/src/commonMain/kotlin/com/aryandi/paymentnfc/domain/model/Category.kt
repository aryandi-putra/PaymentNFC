package com.aryandi.paymentnfc.domain.model

/**
 * Domain model for Category
 * Represents a card category (e.g., Debit/Credit, Member Card, E-Money)
 */
data class Category(
    val id: String,
    val name: String,
    val displayName: String,
    val iconName: String? = null,
    val sortOrder: Int = 0
) {
    companion object {
        // Default category IDs for backward compatibility
        const val DEBIT_CREDIT_ID = "debit_credit"
        const val MEMBER_CARD_ID = "member_card"
        const val ELECTRONIC_MONEY_ID = "electronic_money"
        
        /**
         * Default categories to seed when database is empty
         */
        fun defaultCategories(): List<Category> = listOf(
            Category(
                id = DEBIT_CREDIT_ID,
                name = "DEBIT_CREDIT",
                displayName = "Debit/Credit Card",
                iconName = "credit_card",
                sortOrder = 0
            ),
            Category(
                id = MEMBER_CARD_ID,
                name = "MEMBER_CARD",
                displayName = "Member Card",
                iconName = "card_membership",
                sortOrder = 1
            ),
            Category(
                id = ELECTRONIC_MONEY_ID,
                name = "ELECTRONIC_MONEY",
                displayName = "Electronic Money Card",
                iconName = "account_balance_wallet",
                sortOrder = 2
            )
        )
    }
}
