package com.aryandi.paymentnfc.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Sign Up Screen - Placeholder Implementation
 * This will be a fully functional sign up form in a complete implementation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SignUpScreen(
    onBack: () -> Unit = {},
    onSignUp: (String, String, String) -> Unit = { _, _, _ -> },
    onSignIn: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF4285F4)
    val titleColor = Color(0xFF3F3D56)
    val subtitleColor = Color(0xFF707070)
    val textFieldBg = Color(0xFFFFFFFF)
    val textFieldBorder = Color(0xFFE0E0E0)
    val gradientColors = listOf(Color(0xFF6C63FF), Color(0xFF3F3D56))

    Scaffold(
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
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
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

                    // Title Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = titleColor
                        )
                        Text(
                            text = "Hello there, create New account",
                            fontSize = 14.sp,
                            color = subtitleColor,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Illustration Placeholder
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Light background circle
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFF5F5F5))
                        )

                        // Colorful dots (simulated)
                        Dot(Color(0xFF6C63FF), Alignment.TopStart, 20.dp)
                        Dot(Color(0xFFFF708D), Alignment.TopEnd, 25.dp)
                        Dot(Color(0xFFFFB74D), Alignment.BottomStart, 30.dp)
                        Dot(Color(0xFF00B0FF), Alignment.BottomEnd, 15.dp)
                        Dot(Color(0xFF4DB6AC), Alignment.CenterStart, 18.dp)

                        // Phone Icon
                        Card(
                            modifier = Modifier.size(60.dp, 100.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFF4FACFE), Color(0xFF8E54E9))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Smartphone,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Name Field
                    SignUpTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Name"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    SignUpTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    SignUpTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        isPassword = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Field
                    SignUpTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = "Confirm Password",
                        isPassword = true,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Terms and Conditions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreeToTerms,
                            onCheckedChange = { agreeToTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = primaryBlue
                            )
                        )
                        Text(
                            text = "By creating an account your aggree\nto our Term and Condtions",
                            fontSize = 12.sp,
                            color = subtitleColor
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign Up Button
                    Button(
                        onClick = { 
                            if (validateSignUp(name, email, password, confirmPassword)) {
                                onSignUp(name, email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = gradientColors
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sign Up",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Footer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Have an account? ",
                            color = subtitleColor,
                            fontSize = 14.sp
                        )
                        TextButton(onClick = onSignIn) {
                            Text(
                                text = "Sign In",
                                color = Color(0xFF3F3D56),
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

@Composable
fun Dot(color: Color, alignment: Alignment, size: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
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
        trailingIcon = trailingIcon ?: if (isPassword) {
            { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) }
        } else null,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFF4285F4),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

/**
 * Simple validation for sign up form
 */
private fun validateSignUp(
    name: String,
    email: String,
    password: String,
    confirmPassword: String
): Boolean {
    return name.isNotBlank() && 
           email.isNotBlank() && 
           password.isNotBlank() && 
           password == confirmPassword
}