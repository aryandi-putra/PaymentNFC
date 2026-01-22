package com.aryandi.paymentnfc.features.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterEvent
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterIntent
import com.aryandi.paymentnfc.presentation.viewmodel.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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

    val primaryBlue = Color(0xFF4285F4)
    val titleColor = Color(0xFF3F3D56)
    val subtitleColor = Color(0xFF707070)
    val gradientColors = listOf(Color(0xFF6C63FF), Color(0xFF3F3D56))

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = primaryBlue,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Sign up",
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
                windowInsets = WindowInsets.statusBars
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

                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SignUpTextField(
                        value = uiState.username,
                        onValueChange = { viewModel.onIntent(RegisterIntent.UsernameChanged(it)) },
                        placeholder = "Username"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignUpTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.onIntent(RegisterIntent.EmailChanged(it)) },
                        placeholder = "Email"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignUpTextField(
                        value = uiState.firstName,
                        onValueChange = { viewModel.onIntent(RegisterIntent.FirstNameChanged(it)) },
                        placeholder = "First Name"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignUpTextField(
                        value = uiState.lastName,
                        onValueChange = { viewModel.onIntent(RegisterIntent.LastNameChanged(it)) },
                        placeholder = "Last Name"
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.onIntent(RegisterIntent.Submit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(gradientColors)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = "Sign Up",
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Have an account? ", color = subtitleColor, fontSize = 14.sp)
                        TextButton(onClick = onSignIn) {
                            Text(
                                text = "Sign In",
                                color = titleColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFF4285F4),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}