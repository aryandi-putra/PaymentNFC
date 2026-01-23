package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Empty State Component for Card List
 * Shows when user has no cards added yet
 */
@Composable
fun EmptyCardState(
    modifier: Modifier = Modifier,
    onAddCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = AppColors.TextGray.copy(alpha = 0.3f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon Container
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // Card/Receipt Icon
                Icon(
                    imageVector = Icons.Outlined.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = AppColors.PrimaryBlue.copy(alpha = 0.6f)
                )
                
                // Error/Close Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            color = Color(0xFFE57373),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title
            Text(
                text = "Add your first card",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.TextPrimary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Store member, credit, or debit card.",
                fontSize = 14.sp,
                color = AppColors.TextGray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Add Card Button
            Button(
                onClick = onAddCardClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.PrimaryBlue
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Add your first card",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "→",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyCardStatePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.BackgroundWhite)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        EmptyCardState()
    }
}
