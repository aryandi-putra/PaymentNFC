package com.aryandi.paymentnfc.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aryandi.paymentnfc.database.CategoryEntity
import com.aryandi.paymentnfc.database.PaymentNFCDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Implementation of CategoryLocalDataSource using SQLDelight
 */
class CategoryLocalDataSourceImpl(
    database: PaymentNFCDatabase
) : CategoryLocalDataSource {
    
    private val queries = database.categoryQueries
    
    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }
    
    override suspend fun getCategoryById(id: String): CategoryEntity? = withContext(Dispatchers.IO) {
        queries.selectById(id).executeAsOneOrNull()
    }
    
    override suspend fun getCategoryByName(name: String): CategoryEntity? = withContext(Dispatchers.IO) {
        queries.selectByName(name).executeAsOneOrNull()
    }
    
    override suspend fun insertCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        queries.insertCategory(
            id = category.id,
            name = category.name,
            displayName = category.displayName,
            iconName = category.iconName,
            sortOrder = category.sortOrder
        )
    }
    
    override suspend fun deleteCategory(id: String) = withContext(Dispatchers.IO) {
        queries.deleteById(id)
    }
    
    override suspend fun deleteAllCategories() = withContext(Dispatchers.IO) {
        queries.deleteAll()
    }
    
    override suspend fun updateSortOrder(id: String, sortOrder: Int) = withContext(Dispatchers.IO) {
        queries.updateSortOrder(sortOrder.toLong(), id)
    }
    
    override suspend fun getCategoryCount(): Long = withContext(Dispatchers.IO) {
        queries.countAll().executeAsOne()
    }
}
