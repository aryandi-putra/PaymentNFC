package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.usecase.DeleteCardUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCardByIdUseCase
import com.aryandi.paymentnfc.domain.usecase.GetCardTransactionsUseCase
import com.aryandi.paymentnfc.domain.usecase.SetDefaultCardUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CardDetailIntent {
    data class LoadCard(val cardId: String) : CardDetailIntent
    data object LoadTransactions : CardDetailIntent
    data object SetDefaultPayment : CardDetailIntent
    data object DeleteCard : CardDetailIntent
}

sealed interface CardDetailEvent {
    data object DefaultPaymentSet : CardDetailEvent
    data object CardDeleted : CardDetailEvent
    data class Error(val message: String) : CardDetailEvent
}

data class CardDetailUiState(
    val card: Card? = null,
    val transactions: Map<String, List<Transaction>> = emptyMap(),
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null
)

class CardDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val getCardTransactionsUseCase: GetCardTransactionsUseCase,
    private val setDefaultCardUseCase: SetDefaultCardUseCase,
    private val deleteCardUseCase: DeleteCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()
    
    private val _events = MutableSharedFlow<CardDetailEvent>()
    val events: SharedFlow<CardDetailEvent> = _events.asSharedFlow()

    fun onIntent(intent: CardDetailIntent) {
        when (intent) {
            is CardDetailIntent.LoadCard -> loadCard(intent.cardId)
            CardDetailIntent.LoadTransactions -> loadTransactions()
            CardDetailIntent.SetDefaultPayment -> setDefaultPayment()
            CardDetailIntent.DeleteCard -> deleteCard()
        }
    }
    
    private fun loadCard(cardId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getCardByIdUseCase(cardId).fold(
                onSuccess = { card ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            card = card
                        ) 
                    }
                    // Also load transactions
                    loadTransactions()
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message
                        ) 
                    }
                }
            )
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getCardTransactionsUseCase().fold(
                onSuccess = { transactions ->
                    _uiState.update { 
                        it.copy(
                            transactions = transactions
                        ) 
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            error = error.message
                        ) 
                    }
                }
            )
        }
    }
    
    private fun setDefaultPayment() {
        val cardId = _uiState.value.card?.id ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            
            setDefaultCardUseCase(cardId).fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            isProcessing = false,
                            card = state.card?.copy(isDefault = true)
                        )
                    }
                    _events.emit(CardDetailEvent.DefaultPaymentSet)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isProcessing = false) }
                    _events.emit(CardDetailEvent.Error(error.message ?: "Failed to set default payment"))
                }
            )
        }
    }
    
    private fun deleteCard() {
        val cardId = _uiState.value.card?.id ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true) }
            
            deleteCardUseCase(cardId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isProcessing = false) }
                    _events.emit(CardDetailEvent.CardDeleted)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isProcessing = false) }
                    _events.emit(CardDetailEvent.Error(error.message ?: "Failed to delete card"))
                }
            )
        }
    }
}
