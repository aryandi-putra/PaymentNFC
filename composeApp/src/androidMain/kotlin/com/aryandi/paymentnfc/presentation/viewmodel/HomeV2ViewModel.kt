package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCategoriesUseCase
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.mapper.CardMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    /**
     * Seed default categories and start observing data
     * Using a single coroutine to avoid race conditions
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun seedDefaultCategoriesAndObserve() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Seed default categories if needed
            try {
                getCategoriesUseCase.seedDefaultCategoriesIfNeeded()
            } catch (e: Exception) {
                // Continue even if seeding fails
            }
            
            // Observe categories and cards together in the same coroutine
            getCategoriesUseCase.observeCategories()
                .flatMapLatest { categories ->
                    if (categories.isEmpty()) {
                        // Return a flow with empty list when no categories
                        flowOf(emptyList<HomeCategoryWithCards>())
                    } else {
                        // Switch to cards flow for current categories
                        getCardsUseCase.observeCardsGroupedByCategory(categories)
                            .map { cardsMap ->
                                categories.map { category ->
                                    HomeCategoryWithCards(
                                        category = category,
                                        cards = CardMapper.toCardDataList(cardsMap[category.id] ?: emptyList())
                                    )
                                }
                            }
                    }
                }
                .catch { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load data"
                        ) 
                    }
                }
                .collect { categoriesWithCards ->
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
