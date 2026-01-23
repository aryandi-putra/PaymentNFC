package com.aryandi.paymentnfc.features.addcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.presentation.viewmodel.AddCardEvent
import com.aryandi.paymentnfc.presentation.viewmodel.AddCardIntent
import com.aryandi.paymentnfc.presentation.viewmodel.AddCardViewModel
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

/**
 * Add Debit/Credit Card Form Screen
 * Second step in the add card flow - user fills in card details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDebitCreditCardScreen(
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    viewModel: AddCardViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    
    val categories = listOf(
        "Visa",
        "Mastercard",
        "American Express",
        "JCB"
    )
    
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is AddCardEvent.Success -> onSaveSuccess()
                is AddCardEvent.Failure -> { /* Handle error */ }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        containerColor = AppColors.BackgroundWhite,
        bottomBar = {
            // Save Button at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.BackgroundWhite)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { viewModel.onIntent(AddCardIntent.Submit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(28.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Save",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Debit or Credit Card",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Category Dropdown
            Text(
                text = "Choose category",
                fontSize = 12.sp,
                color = AppColors.TextGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = uiState.cardType.name,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = "Choose category",
                            color = AppColors.TextGray
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = AppColors.TextGray
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                        focusedBorderColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                val type = when(category) {
                                    "Visa" -> CardTypeModel.VISA
                                    "Mastercard" -> CardTypeModel.MASTERCARD
                                    else -> CardTypeModel.VISA
                                }
                                viewModel.onIntent(AddCardIntent.CardTypeChanged(type))
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Card Number Field
            OutlinedTextField(
                value = uiState.cardNumber,
                onValueChange = { viewModel.onIntent(AddCardIntent.CardNumberChanged(it)) },
                placeholder = {
                    Text(
                        text = "Card Number",
                        color = AppColors.TextGray
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CreditCard,
                        contentDescription = "Card",
                        tint = AppColors.TextGray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { /* TODO: Open camera */ }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scan card",
                            tint = AppColors.TextGray
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                    focusedBorderColor = AppColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Expiry Date and CVV Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // MM/YY Field
                OutlinedTextField(
                    value = uiState.expiryDate,
                    onValueChange = { 
                        if (it.length <= 5) viewModel.onIntent(AddCardIntent.ExpiryDateChanged(it))
                    },
                    placeholder = {
                        Text(
                            text = "MM/YY",
                            color = AppColors.TextGray
                        )
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                        focusedBorderColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                // CVV Field
                OutlinedTextField(
                    value = uiState.cvv,
                    onValueChange = { 
                        if (it.length <= 3) viewModel.onIntent(AddCardIntent.CvvChanged(it))
                    },
                    placeholder = {
                        Text(
                            text = "CVV 3 Digits",
                            color = AppColors.TextGray
                        )
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                        focusedBorderColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
fun AddDebitCreditCardScreenPreview() {
    AddDebitCreditCardScreen()
}
