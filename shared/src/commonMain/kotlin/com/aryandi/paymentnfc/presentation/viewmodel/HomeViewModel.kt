package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeIntent {
    data object Refresh : HomeIntent
    data class TransactionClicked(val transaction: Transaction) : HomeIntent
}

data class HomeUiState(
    val userId: String = "",
    val userName: String = "Guest",
    val cardsCount: Int = 3,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val userId: String,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(userId = userId))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> loadHomeData()
            is HomeIntent.TransactionClicked -> {
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = homeRepository.getTransactions()
            
            result.fold(
                onSuccess = { transactions ->
                    _uiState.update { 
                        it.copy(
                            userName = if (userId.contains("@")) userId.substringBefore("@") else userId,
                            cardsCount = 3,
                            transactions = transactions,
                            isLoading = false
                        ) 
                    }
                },
                onFailure = { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load transactions"
                        ) 
                    }
                }
            )
        }
    }
}
