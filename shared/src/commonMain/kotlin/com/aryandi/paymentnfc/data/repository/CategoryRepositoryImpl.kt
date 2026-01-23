package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.local.CategoryLocalDataSource
import com.aryandi.paymentnfc.data.mapper.CategoryMapper
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
            entities.map { CategoryMapper.toDomain(it) }
        }
    }
    
    override suspend fun getCategoryById(id: String): Category? {
        return localDataSource.getCategoryById(id)?.let { CategoryMapper.toDomain(it) }
    }
    
    override suspend fun saveCategory(category: Category) {
        localDataSource.insertCategory(CategoryMapper.toEntity(category))
    }
    
    override suspend fun saveCategories(categories: List<Category>) {
        categories.forEach { category ->
            localDataSource.insertCategory(CategoryMapper.toEntity(category))
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
}
