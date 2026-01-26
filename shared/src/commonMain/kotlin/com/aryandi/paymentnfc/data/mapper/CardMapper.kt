package com.aryandi.paymentnfc.data.mapper

import com.aryandi.paymentnfc.database.CardEntity
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardTypeModel

/**
 * Mapper for Card data
 */
object CardMapper {
    fun toDomain(entity: CardEntity): Card {
        return Card(
            id = entity.id,
            bankName = entity.bankName,
            cardType = CardTypeModel.valueOf(entity.cardType),
            cardNumber = entity.cardNumber,
            maskedNumber = entity.maskedNumber,
            cardHolder = entity.cardHolder,
            categoryId = entity.categoryId,
            colorHex = entity.colorHex,
            isDefault = entity.isDefault == 1L
        )
    }

    fun toEntity(domain: Card): CardEntity {
        return CardEntity(
            id = domain.id,
            bankName = domain.bankName,
            cardType = domain.cardType.name,
            cardNumber = domain.cardNumber,
            maskedNumber = domain.maskedNumber,
            cardHolder = domain.cardHolder,
            categoryId = domain.categoryId,
            colorHex = domain.colorHex,
            isDefault = if (domain.isDefault) 1L else 0L
        )
    }
}
