package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CategoryRepository

/**
 * Use case for adding a new category
 * Saves the category to the local database via CategoryRepository
 */
class AddCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    
    suspend operator fun invoke(category: Category): Result<Unit> {
        return try {
            categoryRepository.saveCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Create and save a category with auto-generated sort order
     */
    suspend fun addCategory(
        id: String,
        name: String,
        displayName: String,
        iconName: String? = null
    ): Result<Category> {
        return try {
            val sortOrder = categoryRepository.getNextSortOrder()
            val category = Category(
                id = id,
                name = name,
                displayName = displayName,
                iconName = iconName,
                sortOrder = sortOrder
            )
            categoryRepository.saveCategory(category)
            Result.success(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
