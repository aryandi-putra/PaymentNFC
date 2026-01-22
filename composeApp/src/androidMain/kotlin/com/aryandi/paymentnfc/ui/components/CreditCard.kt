package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
    isExpanded: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isExpanded) 220.dp else height),
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
                
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Balance
                    Text(
                        text = cardData.maskedNumber, // This is usually "$•••••"
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Card Number
                    Text(
                        text = if (isVisible) cardData.cardNumber else "•••• •••• •••• ••••",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Show Button
                    Surface(
                        onClick = onVisibilityToggle,
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isVisible) "Hide" else "Show",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Card Holder
                    Text(
                        text = cardData.cardHolder,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
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
 * Card Type Logo - Displays VISA or MASTERCARD badge
 */
@Composable
fun CardTypeLogo(cardType: CardType) {
    if (cardType == CardType.MASTERCARD) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .offset(x = (-10).dp)
                    .size(24.dp)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
            )
            Text(
                text = "mastercard",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.offset(x = (-4).dp)
            )
        }
    } else {
        Text(
            text = "VISA",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
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
