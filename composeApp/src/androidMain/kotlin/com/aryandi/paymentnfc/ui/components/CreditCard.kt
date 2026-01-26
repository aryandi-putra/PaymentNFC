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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.R
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview

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
    val id: String = "",
    val bankName: String,
    val cardType: CardType,
    val backgroundColor: Color,
    val cardNumber: String = "**** **** **** ****",
    val maskedNumber: String = "$•••••",
    val cardHolder: String = "",
    val isExpanded: Boolean = false
)

/**
 * Credit Card Component - Unified version for stacked and detailed views
 */
@Composable
fun CreditCard(
    cardData: CardData,
    modifier: Modifier = Modifier,
    height: Dp = 200.dp,
    isVisible: Boolean = false,
    isExpanded: Boolean = false,
    isFullDetail: Boolean = false, // New parameter to handle DetailedCreditCard style
    onVisibilityToggle: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(if (isExpanded) 220.dp else if (isFullDetail) 240.dp else height),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardData.backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullDetail) 24.dp else 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Row: Bank Name and Logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = cardData.bankName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    CardTypeLogo(cardData.cardType)
                }
                
                if (isExpanded || isFullDetail) {
                    Spacer(modifier = Modifier.height(if (isFullDetail) 32.dp else 24.dp))
                    
                    // Balance
                    Text(
                        text = cardData.maskedNumber,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(if (isFullDetail) 16.dp else 12.dp))
                    
                    // Card Number
                    Text(
                        text = if (isVisible) cardData.cardNumber else "•••• •••• •••• ••••",
                        fontSize = if (isFullDetail) 18.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                    
                    if (!isFullDetail) {
                        Spacer(modifier = Modifier.height(16.dp))
                        // Show/Hide Toggle
                        VisibilityToggleButton(isVisible, onVisibilityToggle)
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Bottom Row
                    if (isFullDetail) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.date_placeholder),
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = cardData.cardHolder,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                            
                            Text(
                                text = stringResource(R.string.cvv_placeholder),
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    } else {
                        // Card Holder only for standard expanded view
                        Text(
                            text = cardData.cardHolder,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VisibilityToggleButton(isVisible: Boolean, onToggle: () -> Unit) {
    Surface(
        onClick = onToggle,
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
                text = if (isVisible) stringResource(R.string.hide) else stringResource(R.string.show),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
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
            verticalAlignment = Alignment.CenterVertically
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
                text = stringResource(R.string.mastercard),
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.offset(x = (-4).dp)
            )
        }
    } else {
        Text(
            text = stringResource(R.string.visa),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )
    }
}

/**
 * Simple Credit Card Component - Gradient card with basic info
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Contactless",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = cardNumber, color = Color.White, fontSize = 18.sp)
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

@Preview(showBackground = true)
@Composable
fun CreditCardPreview() {
    val dummyData = CardData(
        bankName = "WeBank",
        cardType = CardType.VISA,
        backgroundColor = Color(0xFF9B59B6),
        cardHolder = "Alexander Parra"
    )
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CreditCard(cardData = dummyData, isExpanded = true)
        CreditCard(cardData = dummyData.copy(cardType = CardType.MASTERCARD), isFullDetail = true)
        SimpleCreditCard()
    }
}
