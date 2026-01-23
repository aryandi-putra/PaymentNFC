package com.aryandi.paymentnfc.features.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.presentation.viewmodel.LoginEvent
import com.aryandi.paymentnfc.presentation.viewmodel.LoginIntent
import com.aryandi.paymentnfc.presentation.viewmodel.LoginViewModel
import com.aryandi.paymentnfc.ui.components.AuthScreenScaffold
import com.aryandi.paymentnfc.ui.components.FilledRoundedTextField
import com.aryandi.paymentnfc.ui.components.GradientButton
import com.aryandi.paymentnfc.ui.theme.AppColors
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSignInSuccess: (userId: String) -> Unit = {},
    onSignUp: () -> Unit = {},
    onForgotPassword: (String) -> Unit = {},
) {
    val viewModel: LoginViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> onSignInSuccess(event.user.username)
                is LoginEvent.LoginFailure -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    AuthScreenScaffold(
        title = "Sign in",
        onBack = onBack,
        snackbarHostState = snackbarHostState
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Welcome text
        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.TextPrimary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Hello there, sign in to continue",
            fontSize = 14.sp,
            color = AppColors.TextSecondary,
            modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Illustration placeholder
        Box(
            modifier = Modifier
                .size(160.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = CircleShape,
                color = AppColors.BackgroundLightBlue,
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Lock",
                        tint = AppColors.PrimaryBlueDark,
                        modifier = Modifier.size(64.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Username field
        FilledRoundedTextField(
            value = uiState.username,
            onValueChange = {
                viewModel.onIntent(LoginIntent.UsernameChanged(it))
                if (uiState.error != null) viewModel.onIntent(LoginIntent.ClearError)
            },
            placeholder = "Username",
            enabled = !uiState.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password field
        FilledRoundedTextField(
            value = uiState.password,
            onValueChange = {
                viewModel.onIntent(LoginIntent.PasswordChanged(it))
                if (uiState.error != null) viewModel.onIntent(LoginIntent.ClearError)
            },
            placeholder = "Password",
            enabled = !uiState.isLoading,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        uiState.error?.let { error ->
            Text(
                text = error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                color = AppColors.ErrorRed,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Text(
            text = "Forgot your password ?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .clickable { onForgotPassword(uiState.username) },
            textAlign = TextAlign.End,
            color = AppColors.TextLight,
            fontSize = 12.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign in button
        GradientButton(
            text = "Sign in",
            onClick = { viewModel.onIntent(LoginIntent.Submit) },
            isLoading = uiState.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Don't have an account?",
                color = AppColors.TextSecondary,
                fontSize = 14.sp,
            )
            TextButton(onClick = onSignUp) {
                Text(
                    text = "Sign Up",
                    color = AppColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}
