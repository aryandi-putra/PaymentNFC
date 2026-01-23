package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.repository.CardRepository
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for CardsScreen
 * Manages card data state and business logic for the Cards tab
 * Uses reactive Flow to automatically update when cards change in database
 */
sealed interface CardsIntent {
    data object LoadCards : CardsIntent
    data object Refresh : CardsIntent
    data object ToggleEditMode : CardsIntent
    data class DeleteCategory(val category: CardCategory) : CardsIntent
    data class DeleteCard(val cardId: String) : CardsIntent
}

data class CardsUiState(
    val debitCreditCards: List<Card> = emptyList(),
    val memberCards: List<Card> = emptyList(),
    val eMoneyCards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false
)

class CardsViewModel(
    private val getCardsUseCase: GetCardsUseCase,
    private val cardRepository: CardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        observeCards()
    }

    fun onIntent(intent: CardsIntent) {
        when (intent) {
            CardsIntent.LoadCards -> { /* Cards are observed reactively, no manual load needed */ }
            CardsIntent.Refresh -> { /* Cards are observed reactively, no manual refresh needed */ }
            CardsIntent.ToggleEditMode -> toggleEditMode()
            is CardsIntent.DeleteCategory -> deleteCategory(intent.category)
            is CardsIntent.DeleteCard -> deleteCard(intent.cardId)
        }
    }

    /**
     * Observe cards from database reactively
     * Updates automatically when cards are added, updated, or deleted
     */
    private fun observeCards() {
        viewModelScope.launch {
            // Observe card changes - starts with empty state if no cards exist
            getCardsUseCase.observeCards()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        ) 
                    }
                }
                .collect { cardsMap ->
                    _uiState.update { 
                        it.copy(
                            debitCreditCards = cardsMap[CardCategory.RETAIL_SHOPPING] ?: emptyList(),
                            memberCards = cardsMap[CardCategory.MEMBER_CARD] ?: emptyList(),
                            eMoneyCards = cardsMap[CardCategory.ELECTRONIC_MONEY] ?: emptyList(),
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

    private fun deleteCategory(category: CardCategory) {
        viewModelScope.launch {
            try {
                cardRepository.deleteCardsByCategory(category)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
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
