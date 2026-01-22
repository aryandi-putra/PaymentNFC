package com.aryandi.paymentnfc.features.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

// Colors
val PrimaryBlue = Color(0xFF2E7DED)
val BackgroundWhite = Color(0xFFFFFFFF)
val BackgroundGray = Color(0xFFF5F7FA)
val CardGreen = Color(0xFF66D4A8)
val CardOrange = Color(0xFFFF9F6E)
val CardYellow = Color(0xFFFFD068)
val CardPurple = Color(0xFF8FA4FF)
val TextGray = Color(0xFF8E8E93)

/**
 * Cards Screen - Beautiful implementation based on design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CardsScreen(
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    var isCardNumberVisible by remember { mutableStateOf(false) }
    
    val cards = remember {
        listOf(
            CardData(
                bankName = "NiceBank",
                cardType = CardType.MASTERCARD,
                backgroundColor = CardGreen,
                isExpanded = false
            ),
            CardData(
                bankName = "AriaBank",
                cardType = CardType.VISA,
                backgroundColor = CardOrange,
                isExpanded = false
            ),
            CardData(
                bankName = "AriaBank",
                cardType = CardType.MASTERCARD,
                backgroundColor = CardYellow,
                isExpanded = false
            ),
            CardData(
                bankName = "TrustBank",
                cardType = CardType.VISA,
                backgroundColor = CardPurple,
                cardNumber = "**** **** **** 1234",
                maskedNumber = "$•••••",
                cardHolder = "Alexander Parra",
                isExpanded = true
            )
        )
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
                actions = {
                    TextButton(onClick = onEdit) {
                        Text(
                            text = "Edit",
                            color = PrimaryBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite
                )
            )
        },
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cards",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        icon = Icons.Default.CreditCard,
                        label = "Add Card",
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        icon = Icons.Default.Category,
                        label = "Add Categories",
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Debit/Credit Card Section
            item {
                SectionHeader(title = "Debit/Credit Card", actionText = "Add Card")
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Stacked Cards
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    cards.forEachIndexed { index, card ->
                        val offsetY = (index * 50).dp
                        CreditCard(
                            cardData = card,
                            modifier = Modifier.offset(y = offsetY),
                            isVisible = isCardNumberVisible,
                            onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Member Card Section
            item {
                SectionHeader(title = "Member Card", actionText = "Add Card")
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ActionCard(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = BackgroundGray),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryBlue,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        TextButton(onClick = onActionClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = actionText,
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CreditCard(
    cardData: CardData,
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
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
                    Column {
                        // Card Number
                        Text(
                            text = if (isVisible) cardData.cardNumber else cardData.maskedNumber,
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
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Show", fontSize = 12.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
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
            
            // Card Type Logo
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                CardTypeLogo(cardData.cardType)
            }
        }
    }
}

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

@Composable
fun BottomNavBar() {
    NavigationBar(
        containerColor = BackgroundWhite,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.CreditCard, contentDescription = "Cards") },
            label = { Text("Cards") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI") },
            label = { Text("AI") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

// Data Models
data class CardData(
    val bankName: String,
    val cardType: CardType,
    val backgroundColor: Color,
    val cardNumber: String = "**** **** **** ****",
    val maskedNumber: String = "$•••••",
    val cardHolder: String = "",
    val isExpanded: Boolean = false
)

enum class CardType {
    VISA,
    MASTERCARD
}
