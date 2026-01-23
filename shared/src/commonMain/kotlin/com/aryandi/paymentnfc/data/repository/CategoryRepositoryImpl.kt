package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.local.CategoryLocalDataSource
import com.aryandi.paymentnfc.database.CategoryEntity
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of CategoryRepository using local data source
 */
class CategoryRepositoryImpl(
    private val localDataSource: CategoryLocalDataSource
) : CategoryRepository {
    
    override fun getAllCategories(): Flow<List<Category>> {
        return localDataSource.getAllCategories().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getCategoryById(id: String): Category? {
        return localDataSource.getCategoryById(id)?.toDomainModel()
    }
    
    override suspend fun saveCategory(category: Category) {
        localDataSource.insertCategory(category.toEntity())
    }
    
    override suspend fun saveCategories(categories: List<Category>) {
        categories.forEach { category ->
            localDataSource.insertCategory(category.toEntity())
        }
    }
    
    override suspend fun deleteCategory(id: String) {
        localDataSource.deleteCategory(id)
    }
    
    override suspend fun deleteAllCategories() {
        localDataSource.deleteAllCategories()
    }
    
    override suspend fun seedDefaultCategoriesIfNeeded() {
        val count = localDataSource.getCategoryCount()
        if (count == 0L) {
            saveCategories(Category.defaultCategories())
        }
    }
    
    override suspend fun getNextSortOrder(): Int {
        val categories = localDataSource.getAllCategories().first()
        return if (categories.isEmpty()) 0 else categories.maxOf { it.sortOrder.toInt() } + 1
    }
    
    // Extension functions for mapping
    private fun CategoryEntity.toDomainModel(): Category {
        return Category(
            id = id,
            name = name,
            displayName = displayName,
            iconName = iconName,
            sortOrder = sortOrder.toInt()
        )
    }
    
    private fun Category.toEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            name = name,
            displayName = displayName,
            iconName = iconName,
            sortOrder = sortOrder.toLong()
        )
    }
}
