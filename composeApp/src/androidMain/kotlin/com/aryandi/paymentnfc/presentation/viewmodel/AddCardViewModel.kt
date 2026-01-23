package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
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
    data class CategoryChanged(val value: String) : AddCardIntent
    data class CardTypeChanged(val value: CardTypeModel) : AddCardIntent
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
    val category: String = "", // For "Others" card dropdown
    val cardType: CardTypeModel = CardTypeModel.VISA,
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

    fun onIntent(intent: AddCardIntent) {
        when (intent) {
            is AddCardIntent.CardNumberChanged -> _uiState.update { it.copy(cardNumber = intent.value) }
            is AddCardIntent.ExpiryDateChanged -> _uiState.update { it.copy(expiryDate = intent.value) }
            is AddCardIntent.CvvChanged -> _uiState.update { it.copy(cvv = intent.value) }
            is AddCardIntent.CategoryChanged -> _uiState.update { it.copy(category = intent.value) }
            is AddCardIntent.CardTypeChanged -> _uiState.update { it.copy(cardType = intent.value) }
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
            // Determine category based on input or default
            val cardCategory = if (currentState.category.isNotBlank()) {
                // Map string category to enum if possible, or default
                when(currentState.category) {
                    "Retail & Shopping" -> CardCategory.RETAIL_SHOPPING
                    "Member Card" -> CardCategory.MEMBER_CARD
                    "Electronic Money" -> CardCategory.ELECTRONIC_MONEY
                    else -> CardCategory.RETAIL_SHOPPING
                }
            } else {
                CardCategory.RETAIL_SHOPPING
            }

            val card = Card(
                id = UUID.randomUUID().toString(),
                bankName = "New Card", // Placeholder
                cardType = currentState.cardType,
                cardNumber = currentState.cardNumber,
                maskedNumber = "**** ${currentState.cardNumber.takeLast(4)}",
                cardHolder = "User", // Placeholder
                category = cardCategory,
                colorHex = "#FF6B4A" // Default color
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
}
