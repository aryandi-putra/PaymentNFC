package com.aryandi.paymentnfc.domain.repository

import com.aryandi.paymentnfc.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Category operations
 * Defines the contract for category data management
 */
interface CategoryRepository {
    
    /**
     * Get all categories as a Flow
     */
    fun getAllCategories(): Flow<List<Category>>
    
    /**
     * Get a single category by ID
     */
    suspend fun getCategoryById(id: String): Category?
    
    /**
     * Insert or update a category
     */
    suspend fun saveCategory(category: Category)
    
    /**
     * Insert multiple categories
     */
    suspend fun saveCategories(categories: List<Category>)
    
    /**
     * Delete a category by ID
     */
    suspend fun deleteCategory(id: String)
    
    /**
     * Delete all categories
     */
    suspend fun deleteAllCategories()
    
    /**
     * Seed default categories if database is empty
     */
    suspend fun seedDefaultCategoriesIfNeeded()
    
    /**
     * Get the next sort order for a new category
     */
    suspend fun getNextSortOrder(): Int
}
