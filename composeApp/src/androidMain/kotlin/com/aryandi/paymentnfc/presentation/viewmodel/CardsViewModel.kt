package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.repository.CardRepository
import com.aryandi.paymentnfc.domain.usecase.AddCategoryUseCase
import com.aryandi.paymentnfc.domain.usecase.DeleteCategoryUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for CardsScreen
 * Manages card and category data with reactive updates from database
 */
sealed interface CardsIntent {
    data object ToggleEditMode : CardsIntent
    data class DeleteCategory(val categoryId: String) : CardsIntent
    data class DeleteCard(val cardId: String) : CardsIntent
    data class AddCategory(val displayName: String) : CardsIntent
    data object ShowAddCategorySheet : CardsIntent
    data object HideAddCategorySheet : CardsIntent
    data object DismissCategoryCreatedDialog : CardsIntent
}

sealed interface CardsEvent {
    data object CategoryCreated : CardsEvent
    data class Error(val message: String) : CardsEvent
}

/**
 * Represents a category with its cards for UI display
 */
data class CategoryWithCards(
    val category: Category,
    val cards: List<Card>
)

data class CardsUiState(
    val categoriesWithCards: List<CategoryWithCards> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val isAddCategorySheetVisible: Boolean = false,
    val isAddingCategory: Boolean = false,
    val showCategoryCreatedDialog: Boolean = false
)

class CardsViewModel(
    private val getCardsUseCase: GetCardsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()
    
    private val _events = MutableSharedFlow<CardsEvent>()
    val events: SharedFlow<CardsEvent> = _events.asSharedFlow()

    init {
        seedDefaultCategoriesAndObserve()
    }

    fun onIntent(intent: CardsIntent) {
        when (intent) {
            CardsIntent.ToggleEditMode -> toggleEditMode()
            is CardsIntent.DeleteCategory -> deleteCategory(intent.categoryId)
            is CardsIntent.DeleteCard -> deleteCard(intent.cardId)
            is CardsIntent.AddCategory -> addCategory(intent.displayName)
            CardsIntent.ShowAddCategorySheet -> _uiState.update { it.copy(isAddCategorySheetVisible = true) }
            CardsIntent.HideAddCategorySheet -> _uiState.update { it.copy(isAddCategorySheetVisible = false) }
            CardsIntent.DismissCategoryCreatedDialog -> _uiState.update { it.copy(showCategoryCreatedDialog = false) }
        }
    }

    private fun seedDefaultCategoriesAndObserve() {
        viewModelScope.launch {
            // Seed default categories if needed
            getCategoriesUseCase.seedDefaultCategoriesIfNeeded()
            
            // Observe categories and cards together
            observeCategoriesWithCards()
        }
    }

    /**
     * Observe categories and cards from database reactively
     * Combines category and card flows to create CategoryWithCards list
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
                    // Now observe cards grouped by these categories
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
                        CategoryWithCards(
                            category = category,
                            cards = cardsMap[category.id] ?: emptyList()
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

    private fun toggleEditMode() {
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }

    private fun addCategory(displayName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingCategory = true) }
            
            val id = UUID.randomUUID().toString()
            val name = displayName.uppercase().replace(" ", "_")
            
            addCategoryUseCase.addCategory(
                id = id,
                name = name,
                displayName = displayName
            ).fold(
                onSuccess = {
                    _uiState.update { 
                        it.copy(
                            isAddingCategory = false,
                            isAddCategorySheetVisible = false,
                            showCategoryCreatedDialog = true
                        ) 
                    }
                    _events.emit(CardsEvent.CategoryCreated)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isAddingCategory = false, error = error.message) }
                    _events.emit(CardsEvent.Error(error.message ?: "Failed to add category"))
                }
            )
        }
    }

    private fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            deleteCategoryUseCase(categoryId).fold(
                onSuccess = { /* UI updates automatically via Flow */ },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    private fun deleteCard(cardId: String) {
        viewModelScope.launch {
            try {
                cardRepository.deleteCard(cardId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}
