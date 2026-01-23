package com.aryandi.paymentnfc.data.local

import com.aryandi.paymentnfc.database.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for Category operations
 * Abstracts SQLDelight database operations for categories
 */
interface CategoryLocalDataSource {
    
    /**
     * Get all categories from the database
     */
    fun getAllCategories(): Flow<List<CategoryEntity>>
    
    /**
     * Get a single category by ID
     */
    suspend fun getCategoryById(id: String): CategoryEntity?
    
    /**
     * Get a category by name
     */
    suspend fun getCategoryByName(name: String): CategoryEntity?
    
    /**
     * Insert or update a category
     */
    suspend fun insertCategory(category: CategoryEntity)
    
    /**
     * Delete a category by ID
     */
    suspend fun deleteCategory(id: String)
    
    /**
     * Delete all categories
     */
    suspend fun deleteAllCategories()
    
    /**
     * Update category sort order
     */
    suspend fun updateSortOrder(id: String, sortOrder: Int)
    
    /**
     * Get category count
     */
    suspend fun getCategoryCount(): Long
}
