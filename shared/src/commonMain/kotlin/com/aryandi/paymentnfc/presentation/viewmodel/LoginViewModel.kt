package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.LoginUseCase
import com.aryandi.paymentnfc.logging.KermitLogger
import com.aryandi.paymentnfc.util.launchSafe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * MVI:
 * - **Intent**: user/system actions coming into the ViewModel (single entrypoint via [onIntent])
 * - **State**: long-lived UI state exposed as [StateFlow]
 * - **Event**: one-off effects (snackbar, navigation) exposed as [SharedFlow]
 */
sealed interface LoginIntent {
    data class UsernameChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object Submit : LoginIntent
    data object ClearError : LoginIntent
    data object Reset : LoginIntent
}

sealed interface LoginEvent {
    data class LoginSuccess(val user: User) : LoginEvent
    data class LoginFailure(val message: String) : LoginEvent
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>(replay = 1, extraBufferCapacity = 1)
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UsernameChanged -> _uiState.update {
                it.copy(username = intent.value)
            }

            is LoginIntent.PasswordChanged -> _uiState.update {
                it.copy(password = intent.value)
            }

            LoginIntent.Submit -> submitLogin()

            LoginIntent.ClearError -> clearError()

            LoginIntent.Reset -> resetState()
        }
    }

    private fun submitLogin() {
        viewModelScope.launchSafe {
            val (username, password) = uiState.value.let { it.username to it.password }

            // Reset state and show loading
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    error = null,
                    isSuccess = false
                ) 
            }
            
            // Execute login use case and log attempt
            KermitLogger.d("Attempting login for user: $username", tag = "LoginViewModel")
            val result = loginUseCase(LoginUseCase.Params(username, password))
            
            // Update state based on result
            result.fold(
                onSuccess = { user ->
                    KermitLogger.i("Login successful for user: $username", tag = "LoginViewModel")
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            user = user,
                            error = null,
                            isSuccess = true
                        ) 
                    }
                    _events.tryEmit(LoginEvent.LoginSuccess(user))
                },
                onFailure = { exception ->
                    val message = exception.message ?: "An unknown error occurred"
                    KermitLogger.e("Login failed for user: $username, error: $message", tag = "LoginViewModel", throwable = exception)
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = message,
                            isSuccess = false
                        ) 
                    }
                    _events.tryEmit(LoginEvent.LoginFailure(message))
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}
