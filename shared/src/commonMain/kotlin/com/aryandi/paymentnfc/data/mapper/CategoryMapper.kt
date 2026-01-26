package com.aryandi.paymentnfc.data.mapper

import com.aryandi.paymentnfc.database.CategoryEntity
import com.aryandi.paymentnfc.domain.model.Category

/**
 * Mapper for Category data
 */
object CategoryMapper {
    fun toDomain(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name,
            displayName = entity.displayName,
            iconName = entity.iconName,
            sortOrder = entity.sortOrder.toInt()
        )
    }

    fun toEntity(domain: Category): CategoryEntity {
        return CategoryEntity(
            id = domain.id,
            name = domain.name,
            displayName = domain.displayName,
            iconName = domain.iconName,
            sortOrder = domain.sortOrder.toLong()
        )
    }
}
