package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.Card
import com.aryandi.paymentnfc.domain.model.CardCategory
import com.aryandi.paymentnfc.domain.usecase.GetCardsUseCase
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.mapper.CardMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for HomeV2Screen
 * Manages card data state and business logic
 */
sealed interface HomeV2Intent {
    data object LoadCards : HomeV2Intent
    data object Refresh : HomeV2Intent
}

data class HomeV2UiState(
    val retailCards: List<CardData> = emptyList(),
    val memberCards: List<CardData> = emptyList(),
    val eMoneyCards: List<CardData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeV2ViewModel(
    private val getCardsUseCase: GetCardsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeV2UiState())
    val uiState: StateFlow<HomeV2UiState> = _uiState.asStateFlow()

    init {
        loadCards()
    }

    fun onIntent(intent: HomeV2Intent) {
        when (intent) {
            HomeV2Intent.LoadCards -> loadCards()
            HomeV2Intent.Refresh -> loadCards()
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
                            retailCards = CardMapper.toCardDataList(cardsMap[CardCategory.RETAIL_SHOPPING] ?: emptyList()),
                            memberCards = CardMapper.toCardDataList(cardsMap[CardCategory.MEMBER_CARD] ?: emptyList()),
                            eMoneyCards = CardMapper.toCardDataList(cardsMap[CardCategory.ELECTRONIC_MONEY] ?: emptyList()),
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
}
