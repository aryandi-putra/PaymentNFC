package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.usecase.GetCardTransactionsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CardDetailIntent {
    data object LoadTransactions : CardDetailIntent
}

data class CardDetailUiState(
    val transactions: Map<String, List<Transaction>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CardDetailViewModel(
    private val getCardTransactionsUseCase: GetCardTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            getCardTransactionsUseCase().fold(
                onSuccess = { transactions ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            transactions = transactions
                        ) 
                    }
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
}
