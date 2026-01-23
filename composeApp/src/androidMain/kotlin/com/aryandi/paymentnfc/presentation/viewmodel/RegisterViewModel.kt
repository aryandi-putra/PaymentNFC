package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.RegisterUseCase
import com.aryandi.paymentnfc.logging.KermitLogger
import com.aryandi.paymentnfc.util.launchSafe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface RegisterIntent {
    data class UsernameChanged(val value: String) : RegisterIntent
    data class EmailChanged(val value: String) : RegisterIntent
    data class FirstNameChanged(val value: String) : RegisterIntent
    data class LastNameChanged(val value: String) : RegisterIntent
    data object Submit : RegisterIntent
    data object ClearError : RegisterIntent
}

sealed interface RegisterEvent {
    data class RegisterSuccess(val user: User) : RegisterEvent
    data class RegisterFailure(val message: String) : RegisterEvent
}

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RegisterEvent>(replay = 1, extraBufferCapacity = 1)
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.UsernameChanged -> _uiState.update { it.copy(username = intent.value) }
            is RegisterIntent.EmailChanged -> _uiState.update { it.copy(email = intent.value) }
            is RegisterIntent.FirstNameChanged -> _uiState.update { it.copy(firstName = intent.value) }
            is RegisterIntent.LastNameChanged -> _uiState.update { it.copy(lastName = intent.value) }
            RegisterIntent.Submit -> submitRegister()
            RegisterIntent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun submitRegister() {
        val currentState = uiState.value
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launchSafe {
            val params = RegisterUseCase.Params(
                username = currentState.username,
                email = currentState.email,
                firstName = currentState.firstName,
                lastName = currentState.lastName
            )
            
            val result = registerUseCase(params)
            
            result.fold(
                onSuccess = { user ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _events.tryEmit(RegisterEvent.RegisterSuccess(user))
                },
                onFailure = { error ->
                    val message = error.message ?: "Registration failed"
                    _uiState.update { it.copy(isLoading = false, error = message) }
                    _events.tryEmit(RegisterEvent.RegisterFailure(message))
                    KermitLogger.e("Registration failed", tag = "RegisterViewModel", throwable = error)
                }
            )
        }
    }
}
