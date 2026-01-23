package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting all categories
 * Uses CategoryRepository to fetch data from local database
 */
class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    /**
     * Get all categories as a Flow for reactive updates
     */
    fun observeCategories(): Flow<List<Category>> {
        return categoryRepository.getAllCategories()
    }
    
    /**
     * Seed default categories if database is empty
     */
    suspend fun seedDefaultCategoriesIfNeeded() {
        categoryRepository.seedDefaultCategoriesIfNeeded()
    }
}
