package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors

/**
 * Card Type enum for credit card networks
 */
enum class CardType {
    VISA,
    MASTERCARD
}

/**
 * Data class representing credit card information
 */
data class CardData(
    val bankName: String,
    val cardType: CardType,
    val backgroundColor: Color,
    val cardNumber: String = "**** **** **** ****",
    val maskedNumber: String = "$•••••",
    val cardHolder: String = "",
    val isExpanded: Boolean = false
)

/**
 * Detailed Credit Card Component - Shows bank name, card type, and expandable details
 * Used in Cards screen for the stacked card view
 */
@Composable
fun CreditCard(
    cardData: CardData,
    modifier: Modifier = Modifier,
    height: Dp = 200.dp,
    isVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardData.backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Bank Name
                Text(
                    text = cardData.bankName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Card Details (only for expanded card)
                if (cardData.isExpanded) {
                    CardExpandedDetails(
                        cardNumber = cardData.cardNumber,
                        maskedNumber = cardData.maskedNumber,
                        cardHolder = cardData.cardHolder,
                        isVisible = isVisible,
                        onVisibilityToggle = onVisibilityToggle
                    )
                }
            }
            
            // Card Type Logo
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                CardTypeLogo(cardData.cardType)
            }
        }
    }
}

/**
 * Expanded card details section with show/hide functionality
 */
@Composable
private fun CardExpandedDetails(
    cardNumber: String,
    maskedNumber: String,
    cardHolder: String,
    isVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    Column {
        // Card Number
        Text(
            text = if (isVisible) cardNumber else maskedNumber,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Show/Hide Button and Card Holder
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onVisibilityToggle,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                border = null,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) "Hide" else "Show",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Show", fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = cardHolder,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

/**
 * Card Type Logo - Displays VISA or MASTERCARD badge
 */
@Composable
fun CardTypeLogo(cardType: CardType) {
    Box(
        modifier = Modifier
            .size(48.dp, 32.dp)
            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cardType.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

/**
 * Simple Credit Card Component - Gradient card with basic info
 * Used in Home screen for the card carousel
 */
@Composable
fun SimpleCreditCard(
    modifier: Modifier = Modifier,
    cardNumber: String = "***9749",
    maskedBalance: String = "******** P",
    cardBrand: String = "VISA",
    gradientColors: List<Color> = listOf(AppColors.CardGradientStart, AppColors.CardGradientEnd)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = gradientColors))
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Contactless Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Contactless",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Chip and Balance
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Chip placeholder
                Box(
                    modifier = Modifier
                        .size(40.dp, 30.dp)
                        .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                )
                
                Spacer(modifier = Modifier.width(100.dp))
                
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = maskedBalance, color = Color.White)
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Card Number and Brand
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = cardNumber,
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = cardBrand,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}
