package com.aryandi.paymentnfc.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.presentation.viewmodel.LoginEvent
import com.aryandi.paymentnfc.presentation.viewmodel.LoginIntent
import com.aryandi.paymentnfc.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onSignInSuccess: (userId: String) -> Unit = {},
    onSignUp: () -> Unit = {},
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent,
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4FACFE),
                            Color(0xFF8E54E9),
                        ),
                    ),
                ),
        ) {
            // Main white card container
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                color = Color.White,
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    // Top app bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF3C3F7F),
                            )
                        }
                        Text(
                            text = "Sign in",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF3C3F7F),
                            modifier = Modifier.padding(start = 4.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Welcome text
                    Text(
                        text = "Welcome Back",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4248B5),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hello there, sign in to continue",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF9AA0C8),
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Illustration placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF3F4FF),
                            modifier = Modifier.size(140.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF4FACFE),
                                                Color(0xFF8E54E9),
                                            ),
                                        ),
                                    ),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Lock",
                                    tint = Color.White,
                                    modifier = Modifier.size(48.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Username field
                    TextField(
                        value = uiState.username,
                        onValueChange = {
                            viewModel.onIntent(LoginIntent.UsernameChanged(it))
                            if (uiState.error != null) viewModel.onIntent(LoginIntent.ClearError)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Username") },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF6F7FF),
                            focusedContainerColor = Color(0xFFF6F7FF),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color(0xFFF6F7FF),
                        ),
                        shape = RoundedCornerShape(24.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    TextField(
                        value = uiState.password,
                        onValueChange = {
                            viewModel.onIntent(LoginIntent.PasswordChanged(it))
                            if (uiState.error != null) viewModel.onIntent(LoginIntent.ClearError)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(text = "Password") },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF6F7FF),
                            focusedContainerColor = Color(0xFFF6F7FF),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color(0xFFF6F7FF),
                        ),
                        shape = RoundedCornerShape(24.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.error?.let { error ->
                        Text(
                            text = error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            color = Color(0xFFD32F2F),
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = "Forgot your password ?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.End,
                        color = Color(0xFFB0B4D4),
                        fontSize = 12.sp,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign in button
                    Button(
                        onClick = { viewModel.onIntent(LoginIntent.Submit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBFC2F5),
                            contentColor = Color.White,
                        ),
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(text = "Sign in")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Don't have an account?",
                            color = Color(0xFF9AA0C8),
                            fontSize = 14.sp,
                        )
                        TextButton(onClick = onSignUp) {
                            Text(
                                text = "Sign Up",
                                color = Color(0xFF4248B5),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

