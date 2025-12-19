package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Transaction
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
    val userName: String = "John Smith",
    val cardsCount: Int = 3,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val userId: String
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
            _uiState.update { it.copy(isLoading = true) }
            
            // Mocking data loading
            val mockTransactions = listOf(
                Transaction("Currency exchange", "17 Sep 2023 11:21 AM", "$ 350.00", "pending"),
                Transaction("Cash-in", "17 Sep 2023 10:34 AM", "$ 100.00", "confirmed"),
                Transaction("Cashback from purchase", "16 Sep 2023 16:08 PM", "$ 1.75", "confirmed"),
                Transaction("Transfer to card", "16 Sep 2023 11:21 AM", "$ 9000.00", "confirmed"),
                Transaction("Transfer to card", "16 Sep 2023 11:21 AM", "$ 9000.00", "confirmed")
            )
            
            _uiState.update { 
                it.copy(
                    userName = if (userId.contains("@")) userId.substringBefore("@") else userId,
                    cardsCount = 3,
                    transactions = mockTransactions,
                    isLoading = false
                ) 
            }
        }
    }
}
