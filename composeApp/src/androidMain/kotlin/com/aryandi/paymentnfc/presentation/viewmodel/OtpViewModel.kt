package com.aryandi.paymentnfc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.ResendOtpUseCase
import com.aryandi.paymentnfc.domain.usecase.VerifyOtpUseCase
import com.aryandi.paymentnfc.logging.KermitLogger
import com.aryandi.paymentnfc.util.launchSafe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface OtpIntent {
    data class OtpChanged(val value: String) : OtpIntent
    data class EmailChanged(val value: String) : OtpIntent
    data object Submit : OtpIntent
    data object ResendOtp : OtpIntent
    data object ClearError : OtpIntent
}

sealed interface OtpEvent {
    data class OtpSuccess(val user: User) : OtpEvent
    data class OtpFailure(val message: String) : OtpEvent
    data class ResendSuccess(val message: String) : OtpEvent
}

data class OtpUiState(
    val emailOrPhone: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

class OtpViewModel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val resendOtpUseCase: ResendOtpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<OtpEvent>(replay = 1, extraBufferCapacity = 1)
    val events: SharedFlow<OtpEvent> = _events.asSharedFlow()

    fun onIntent(intent: OtpIntent) {
        when (intent) {
            is OtpIntent.EmailChanged -> _uiState.update {
                it.copy(emailOrPhone = intent.value)
            }
            is OtpIntent.OtpChanged -> _uiState.update {
                it.copy(otp = intent.value, error = null)
            }
            OtpIntent.Submit -> submitOtp()
            OtpIntent.ResendOtp -> resendOtp()
            OtpIntent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun submitOtp() {
        viewModelScope.launchSafe {
            val emailOrPhone = uiState.value.emailOrPhone
            val otp = uiState.value.otp

            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = verifyOtpUseCase(VerifyOtpUseCase.Params(emailOrPhone, otp))

            result.fold(
                onSuccess = { user ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _events.tryEmit(OtpEvent.OtpSuccess(user))
                },
                onFailure = { exception ->
                    val message = exception.message ?: "Invalid OTP"
                    _uiState.update { it.copy(isLoading = false, error = message) }
                    _events.tryEmit(OtpEvent.OtpFailure(message))
                }
            )
        }
    }

    private fun resendOtp() {
        viewModelScope.launchSafe {
            val emailOrPhone = uiState.value.emailOrPhone
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = resendOtpUseCase(emailOrPhone)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.tryEmit(OtpEvent.ResendSuccess("OTP has been resent"))
                },
                onFailure = { exception ->
                    val message = exception.message ?: "Failed to resend OTP"
                    _uiState.update { it.copy(isLoading = false, error = message) }
                    _events.tryEmit(OtpEvent.OtpFailure(message))
                }
            )
        }
    }
}
