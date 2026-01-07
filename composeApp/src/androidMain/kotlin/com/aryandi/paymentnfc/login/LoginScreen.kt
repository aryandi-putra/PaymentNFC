package com.aryandi.paymentnfc.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val primaryBlue = Color(0xFF4285F4)
    val titleColor = Color(0xFF3F3D56)
    val subtitleColor = Color(0xFF707070)

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
        containerColor = primaryBlue,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Sign in",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryBlue,
                    scrolledContainerColor = primaryBlue
                ),
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(primaryBlue)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Welcome text
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Welcome Back",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Hello there, sign in to continue",
                            fontSize = 14.sp,
                            color = subtitleColor,
                            modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

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
                            color = Color(0xFFF3F4FF),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Lock",
                                    tint = primaryBlue,
                                    modifier = Modifier.size(64.dp),
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
                        placeholder = { Text(text = "Username", color = Color.Gray) },
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF6F7FF),
                            focusedContainerColor = Color(0xFFF6F7FF),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color(0xFFF6F7FF),
                        ),
                        shape = RoundedCornerShape(16.dp),
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
                        placeholder = { Text(text = "Password", color = Color.Gray) },
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
                        shape = RoundedCornerShape(16.dp),
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
                            .padding(horizontal = 4.dp)
                            .clickable { onForgotPassword(uiState.username) },
                        textAlign = TextAlign.End,
                        color = Color(0xFFB0B4D4),
                        fontSize = 12.sp,
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign in button
                    Button(
                        onClick = { viewModel.onIntent(LoginIntent.Submit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF6C63FF), Color(0xFF3F3D56))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text(
                                    text = "Sign in",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Don't have an account?",
                            color = subtitleColor,
                            fontSize = 14.sp,
                        )
                        TextButton(onClick = onSignUp) {
                            Text(
                                text = "Sign Up",
                                color = titleColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

