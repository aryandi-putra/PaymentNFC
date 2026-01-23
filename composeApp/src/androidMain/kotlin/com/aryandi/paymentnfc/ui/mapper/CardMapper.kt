package com.aryandi.paymentnfc.ui.mapper

import androidx.compose.ui.graphics.Color
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.components.CardType

/**
 * Mapper to convert domain Card model to UI CardData model
 */
object CardMapper {
    
    fun toCardData(card: Card): CardData {
        return CardData(
            bankName = card.bankName,
            cardType = card.cardType.toCardType(),
            backgroundColor = parseColor(card.colorHex),
            cardNumber = card.cardNumber,
            maskedNumber = card.maskedNumber,
            cardHolder = card.cardHolder,
            isExpanded = false
        )
    }
    
    fun toCardDataList(cards: List<Card>): List<CardData> {
        return cards.map { toCardData(it) }
    }
    
    private fun CardTypeModel.toCardType(): CardType {
        return when (this) {
            CardTypeModel.VISA -> CardType.VISA
            CardTypeModel.MASTERCARD -> CardType.MASTERCARD
        }
    }
    
    private fun parseColor(hexColor: String): Color {
        return try {
            val cleanHex = hexColor.removePrefix("#")
            val colorInt = cleanHex.toLong(16)
            
            if (cleanHex.length == 6) {
                // RGB format - add full alpha
                Color(0xFF000000 or colorInt)
            } else {
                // ARGB format
                Color(colorInt)
            }
        } catch (e: Exception) {
            // Default to orange color if parsing fails
            Color(0xFFFF6B4A)
        }
    }
}
