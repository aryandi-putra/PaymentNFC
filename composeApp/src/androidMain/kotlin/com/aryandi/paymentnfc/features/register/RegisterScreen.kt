package com.aryandi.paymentnfc.features.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterEvent
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterIntent
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterViewModel
import com.aryandi.paymentnfc.ui.components.AuthScreenScaffold
import com.aryandi.paymentnfc.ui.components.GradientButton
import com.aryandi.paymentnfc.ui.components.OutlinedRoundedTextField
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onSignUpSuccess: (String) -> Unit = {},
    onSignIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> {
                    onSignUpSuccess(event.user.username)
                }
                is RegisterEvent.RegisterFailure -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    AuthScreenScaffold(
        title = "Sign up",
        onBack = onBack,
        snackbarHostState = snackbarHostState
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedRoundedTextField(
            value = uiState.username,
            onValueChange = { viewModel.onIntent(RegisterIntent.UsernameChanged(it)) },
            placeholder = "Username"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedRoundedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onIntent(RegisterIntent.EmailChanged(it)) },
            placeholder = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedRoundedTextField(
            value = uiState.firstName,
            onValueChange = { viewModel.onIntent(RegisterIntent.FirstNameChanged(it)) },
            placeholder = "First Name"
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedRoundedTextField(
            value = uiState.lastName,
            onValueChange = { viewModel.onIntent(RegisterIntent.LastNameChanged(it)) },
            placeholder = "Last Name"
        )

        Spacer(modifier = Modifier.height(32.dp))

        GradientButton(
            text = "Sign Up",
            onClick = { viewModel.onIntent(RegisterIntent.Submit) },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Have an account? ", color = AppColors.TextSecondary, fontSize = 14.sp)
            TextButton(onClick = onSignIn) {
                Text(
                    text = "Sign In",
                    color = AppColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    Surface {
        SignUpScreen(
            onBack = {},
            onSignUpSuccess = {},
            onSignIn = {}
        )
    }
}