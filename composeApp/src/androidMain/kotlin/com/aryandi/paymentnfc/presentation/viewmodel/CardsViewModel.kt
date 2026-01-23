package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for CardsScreen
 * Manages card data state and business logic for the Cards tab
 */
sealed interface CardsIntent {
    data object LoadCards : CardsIntent
    data object Refresh : CardsIntent
    data object ToggleEditMode : CardsIntent
    data class DeleteCategory(val category: CardCategory) : CardsIntent

    // New Intents
    data class AddCard(val card: Card) : CardsIntent
    data class UpdateCard(val card: Card) : CardsIntent
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
    private val getCardsUseCase: GetCardsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        loadCards()
    }

    fun onIntent(intent: CardsIntent) {
        when (intent) {
            CardsIntent.LoadCards -> loadCards()
            CardsIntent.Refresh -> loadCards()
            CardsIntent.ToggleEditMode -> toggleEditMode()
            is CardsIntent.DeleteCategory -> deleteCategory(intent.category)
            is CardsIntent.AddCard -> TODO()
            is CardsIntent.DeleteCard -> TODO()
            is CardsIntent.UpdateCard -> TODO()
        }
    }

    private fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = getCardsUseCase()
            
            result.fold(
                onSuccess = { cardsMap ->
                    _uiState.update { 
                        it.copy(
                            debitCreditCards = cardsMap[CardCategory.RETAIL_SHOPPING] ?: emptyList(),
                            memberCards = cardsMap[CardCategory.MEMBER_CARD] ?: emptyList(),
                            eMoneyCards = cardsMap[CardCategory.ELECTRONIC_MONEY] ?: emptyList(),
                            isLoading = false
                        ) 
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load cards"
                        ) 
                    }
                }
            )
        }
    }

    private fun toggleEditMode() {
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }

    private fun deleteCategory(category: CardCategory) {
        _uiState.update { state ->
            when (category) {
                CardCategory.RETAIL_SHOPPING -> state.copy(debitCreditCards = emptyList())
                CardCategory.MEMBER_CARD -> state.copy(memberCards = emptyList())
                CardCategory.ELECTRONIC_MONEY -> state.copy(eMoneyCards = emptyList())
            }
        }
    }
}
