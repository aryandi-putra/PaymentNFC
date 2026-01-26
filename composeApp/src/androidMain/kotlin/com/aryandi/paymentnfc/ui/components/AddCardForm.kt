package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.domain.model.Category
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.presentation.viewmodel.AddCardIntent
import com.aryandi.paymentnfc.presentation.viewmodel.AddCardUiState
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.stringResource
import com.aryandi.paymentnfc.R

/**
 * Reusable form for adding different types of cards
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardForm(
    uiState: AddCardUiState,
    onIntent: (AddCardIntent) -> Unit,
    showCardTypeSelection: Boolean = false
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    var cardTypeExpanded by remember { mutableStateOf(false) }
    
    val cardTypes = listOf(
        CardTypeModel.VISA to "Visa",
        CardTypeModel.MASTERCARD to "Mastercard"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Category Dropdown
        FormLabel(stringResource(R.string.choose_category))
        
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = uiState.selectedCategory?.displayName ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = if (uiState.isCategoriesLoading) stringResource(R.string.loading) else stringResource(R.string.choose_category),
                        color = AppColors.TextGray
                    )
                },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = AppColors.TextGray)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                    focusedBorderColor = AppColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isCategoriesLoading
            )
            
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                uiState.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.displayName) },
                        onClick = {
                            onIntent(AddCardIntent.CategorySelected(category))
                            categoryExpanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (showCardTypeSelection) {
            // Card Type Dropdown
            FormLabel(stringResource(R.string.card_type))
            
            ExposedDropdownMenuBox(
                expanded = cardTypeExpanded,
                onExpandedChange = { cardTypeExpanded = !cardTypeExpanded }
            ) {
                OutlinedTextField(
                    value = cardTypes.find { it.first == uiState.cardType }?.second ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = AppColors.TextGray)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                        focusedBorderColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = cardTypeExpanded,
                    onDismissRequest = { cardTypeExpanded = false }
                ) {
                    cardTypes.forEach { (type, name) ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                onIntent(AddCardIntent.CardTypeChanged(type))
                                cardTypeExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Card Number Field
        FormLabel(stringResource(R.string.card_number))
        OutlinedTextField(
            value = uiState.cardNumber,
            onValueChange = { onIntent(AddCardIntent.CardNumberChanged(it)) },
            placeholder = { Text("8129 1111 2121 2111", color = AppColors.TextGray.copy(alpha = 0.5f)) },
            leadingIcon = { Icon(Icons.Outlined.CreditCard, contentDescription = null, tint = AppColors.TextGray) },
            trailingIcon = {
                IconButton(onClick = { /* TODO: Open camera */ }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AppColors.TextGray)
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
        
        // Date and CVV
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                FormLabel(stringResource(R.string.expired_date))
                OutlinedTextField(
                    value = uiState.expiryDate,
                    onValueChange = { if (it.length <= 5) onIntent(AddCardIntent.ExpiryDateChanged(it)) },
                    placeholder = { Text(stringResource(R.string.mm_yy), color = AppColors.TextGray.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                        focusedBorderColor = AppColors.PrimaryBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            
            if (showCardTypeSelection) {
                Column(modifier = Modifier.weight(1f)) {
                    FormLabel(stringResource(R.string.cvv))
                    OutlinedTextField(
                        value = uiState.cvv,
                        onValueChange = { if (it.length <= 3) onIntent(AddCardIntent.CvvChanged(it)) },
                        placeholder = { Text(stringResource(R.string.cvv_3_digits), color = AppColors.TextGray.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = AppColors.TextGray.copy(alpha = 0.3f),
                            focusedBorderColor = AppColors.PrimaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
        
        if (uiState.error != null) {
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = AppColors.TextGray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
