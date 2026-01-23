package com.aryandi.paymentnfc.features.addcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Add Card Type Selection Screen
 * First step in the add card flow - user selects card type
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardTypeScreen(
    onClose: () -> Unit = {},
    onSelectDebitCredit: () -> Unit = {},
    onSelectOthers: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        containerColor = AppColors.BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "How to add card",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Debit or Credit Card Option
            CardTypeOption(
                icon = Icons.Default.CreditCard,
                label = "Debit or Credit Card",
                iconTint = AppColors.PrimaryBlue,
                onClick = onSelectDebitCredit
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Others Card Option
            CardTypeOption(
                icon = Icons.Outlined.CardGiftcard,
                label = "Others Card",
                iconTint = AppColors.PrimaryBlue,
                onClick = onSelectOthers
            )
        }
    }
}

@Composable
fun CardTypeOption(
    icon: ImageVector,
    label: String,
    iconTint: Color = AppColors.PrimaryBlue,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundGray
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun AddCardTypeScreenPreview() {
    AddCardTypeScreen()
}
