package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.repository.CardRepository
import com.aryandi.paymentnfc.domain.repository.CategoryRepository

/**
 * Use case for deleting a category
 * Also deletes all cards associated with the category
 */
class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository
) {
    
    /**
     * Delete a category and all its associated cards
     */
    suspend operator fun invoke(categoryId: String): Result<Unit> {
        return try {
            // First delete all cards in this category
            cardRepository.deleteCardsByCategoryId(categoryId)
            // Then delete the category itself
            categoryRepository.deleteCategory(categoryId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
