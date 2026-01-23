package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCategoriesUseCase
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.mapper.CardMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for HomeV2Screen
 * Manages card data state with dynamic categories
 */

/**
 * Represents a category with its cards for UI display
 */
data class HomeCategoryWithCards(
    val category: Category,
    val cards: List<CardData>
)

data class HomeV2UiState(
    val categoriesWithCards: List<HomeCategoryWithCards> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeV2ViewModel(
    private val getCardsUseCase: GetCardsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeV2UiState())
    val uiState: StateFlow<HomeV2UiState> = _uiState.asStateFlow()

    init {
        seedDefaultCategoriesAndObserve()
    }

    private fun seedDefaultCategoriesAndObserve() {
        viewModelScope.launch {
            // Seed default categories if needed
            getCategoriesUseCase.seedDefaultCategoriesIfNeeded()
            
            // Observe categories
            observeCategoriesWithCards()
        }
    }

    /**
     * Observe categories and cards from database reactively
     */
    private fun observeCategoriesWithCards() {
        viewModelScope.launch {
            getCategoriesUseCase.observeCategories()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load categories"
                        ) 
                    }
                }
                .collect { categories ->
                    observeCardsForCategories(categories)
                }
        }
    }

    private fun observeCardsForCategories(categories: List<Category>) {
        viewModelScope.launch {
            if (categories.isEmpty()) {
                _uiState.update { 
                    it.copy(
                        categoriesWithCards = emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                return@launch
            }
            
            getCardsUseCase.observeCardsGroupedByCategory(categories)
                .catch { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        ) 
                    }
                }
                .collect { cardsMap ->
                    val categoriesWithCards = categories.map { category ->
                        HomeCategoryWithCards(
                            category = category,
                            cards = CardMapper.toCardDataList(cardsMap[category.id] ?: emptyList())
                        )
                    }
                    
                    _uiState.update { 
                        it.copy(
                            categoriesWithCards = categoriesWithCards,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
        }
    }
}
