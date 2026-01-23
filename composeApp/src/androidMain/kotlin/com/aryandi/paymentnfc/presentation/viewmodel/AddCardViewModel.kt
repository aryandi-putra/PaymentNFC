package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.domain.usecase.AddCardUseCase
import com.aryandi.paymentnfc.util.launchSafe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

sealed interface AddCardIntent {
    data class CardNumberChanged(val value: String) : AddCardIntent
    data class ExpiryDateChanged(val value: String) : AddCardIntent
    data class CvvChanged(val value: String) : AddCardIntent
    data class CategoryIdChanged(val value: String) : AddCardIntent
    data class CardTypeChanged(val value: CardTypeModel) : AddCardIntent
    data class BankNameChanged(val value: String) : AddCardIntent
    data object Submit : AddCardIntent
    data object Reset : AddCardIntent
}

sealed interface AddCardEvent {
    data object Success : AddCardEvent
    data class Failure(val message: String) : AddCardEvent
}

data class AddCardUiState(
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val categoryId: String = Category.DEBIT_CREDIT_ID, // Default to debit/credit
    val cardType: CardTypeModel = CardTypeModel.VISA,
    val bankName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AddCardViewModel(
    private val addCardUseCase: AddCardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddCardEvent>()
    val events: SharedFlow<AddCardEvent> = _events.asSharedFlow()
    
    /**
     * Set the category ID for the card being added
     */
    fun setCategoryId(categoryId: String) {
        _uiState.update { it.copy(categoryId = categoryId) }
    }

    fun onIntent(intent: AddCardIntent) {
        when (intent) {
            is AddCardIntent.CardNumberChanged -> _uiState.update { it.copy(cardNumber = intent.value) }
            is AddCardIntent.ExpiryDateChanged -> _uiState.update { it.copy(expiryDate = intent.value) }
            is AddCardIntent.CvvChanged -> _uiState.update { it.copy(cvv = intent.value) }
            is AddCardIntent.CategoryIdChanged -> _uiState.update { it.copy(categoryId = intent.value) }
            is AddCardIntent.CardTypeChanged -> _uiState.update { it.copy(cardType = intent.value) }
            is AddCardIntent.BankNameChanged -> _uiState.update { it.copy(bankName = intent.value) }
            AddCardIntent.Reset -> _uiState.value = AddCardUiState()
            AddCardIntent.Submit -> submitCard()
        }
    }

    private fun submitCard() {
        val currentState = uiState.value
        if (currentState.cardNumber.isBlank()) {
            _uiState.update { it.copy(error = "Card number is required") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launchSafe {
            val card = Card(
                id = UUID.randomUUID().toString(),
                bankName = currentState.bankName.ifBlank { "New Card" },
                cardType = currentState.cardType,
                cardNumber = formatCardNumber(currentState.cardNumber),
                maskedNumber = "**** ${currentState.cardNumber.takeLast(4)}",
                cardHolder = "",
                categoryId = currentState.categoryId,
                colorHex = getRandomCardColor()
            )

            addCardUseCase(card).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(AddCardEvent.Success)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                    _events.emit(AddCardEvent.Failure(error.message ?: "Unknown error"))
                }
            )
        }
    }
    
    private fun formatCardNumber(number: String): String {
        val digits = number.filter { it.isDigit() }
        return if (digits.length >= 4) {
            "**** **** **** ${digits.takeLast(4)}"
        } else {
            "**** **** **** ****"
        }
    }
    
    private fun getRandomCardColor(): String {
        val colors = listOf(
            "#FF6B4A", // Orange
            "#FFD93D", // Yellow
            "#A4B494", // Olive
            "#8B6F47", // Brown
            "#2D5F3F", // Dark Green
            "#1E3A5F", // Navy
            "#4A90E2", // Blue
            "#9B59B6"  // Purple
        )
        return colors.random()
    }
}
