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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.components.AppBottomNavBar
import com.aryandi.paymentnfc.ui.components.BottomNavTab
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.components.CardType
import com.aryandi.paymentnfc.ui.components.SectionHeader
import com.aryandi.paymentnfc.ui.components.StackedCardList
import com.aryandi.paymentnfc.ui.theme.AppColors

/**
 * Cards Screen - Beautiful implementation based on design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCardDetail: () -> Unit = {},
    onAddCard: () -> Unit = {}
) {
    var isCardNumberVisible by remember { mutableStateOf(true) }
    
    val debitCreditCards = remember {
        listOf(
            CardData(
                bankName = "NiceBank",
                cardType = CardType.MASTERCARD,
                backgroundColor = AppColors.CardGreen,
                cardNumber = "**** **** **** 1234",
                maskedNumber = "$2,500.00",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "AriaBank",
                cardType = CardType.VISA,
                backgroundColor = AppColors.CardOrange,
                cardNumber = "**** **** **** 5678",
                maskedNumber = "$1,850.50",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "AriaBank",
                cardType = CardType.MASTERCARD,
                backgroundColor = AppColors.CardYellow,
                cardNumber = "**** **** **** 9012",
                maskedNumber = "$3,200.75",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "WeBank",
                cardType = CardType.VISA,
                backgroundColor = AppColors.CardOlive,
                cardNumber = "**** **** **** 3456",
                maskedNumber = "$950.00",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "TrustBank",
                cardType = CardType.VISA,
                backgroundColor = AppColors.CardPurple,
                cardNumber = "**** **** **** 7890",
                maskedNumber = "$5,420.25",
                cardHolder = "Alexander Parra",
                isExpanded = true
            )
        )
    }

    val memberCards = remember {
        listOf(
            CardData(
                bankName = "Alfagift",
                cardType = CardType.MASTERCARD, // Placeholder for gift icon
                backgroundColor = AppColors.CardBrown,
                cardNumber = "**** **** **** 1111",
                maskedNumber = "1,250 pts",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "IKEA",
                cardType = CardType.VISA, // Placeholder for IKEA logo
                backgroundColor = AppColors.CardYellow,
                cardNumber = "**** **** **** 2222",
                maskedNumber = "560 pts",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "MAP",
                cardType = CardType.MASTERCARD, // Placeholder for MAP logo
                backgroundColor = AppColors.CardRed,
                cardNumber = "**** **** **** 3333",
                maskedNumber = "3,400 pts",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "Starbucks",
                cardType = CardType.VISA, // Placeholder for Starbucks logo
                backgroundColor = AppColors.CardDarkGreen,
                cardNumber = "**** **** **** 5678",
                maskedNumber = "$125.50",
                cardHolder = "Alexander Parra",
                isExpanded = true
            )
        )
    }

    val electronicMoneyCards = remember {
        listOf(
            CardData(
                bankName = "BNI Tapcash",
                cardType = CardType.MASTERCARD, // Placeholder for Tapcash logo
                backgroundColor = AppColors.CardOrange,
                cardNumber = "**** **** **** 4444",
                maskedNumber = "$85.00",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "Mandiri E-Money",
                cardType = CardType.VISA, // Placeholder for e-money logo
                backgroundColor = AppColors.CardNavy,
                cardNumber = "**** **** **** 5555",
                maskedNumber = "$120.50",
                cardHolder = "Alexander Parra",
                isExpanded = true
            ),
            CardData(
                bankName = "Flazz BCA",
                cardType = CardType.MASTERCARD, // Placeholder for Flazz logo
                backgroundColor = AppColors.CardBlue,
                cardNumber = "**** **** **** 9012",
                maskedNumber = "$250.75",
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
                            color = AppColors.PrimaryBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        bottomBar = { AppBottomNavBar(
            selectedTab = BottomNavTab.CARDS,
            onHomeClick = onNavigateToHome
        ) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundWhite)
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
                        modifier = Modifier.weight(1f),
                        onClick = onAddCard
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
                SectionHeader(
                    title = "Debit/Credit Card", 
                    actionText = "Add Card",
                    onActionClick = onAddCard
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Stacked Cards - Debit/Credit
            item {
                StackedCardList(
                    cards = debitCreditCards,
                    isCardNumberVisible = isCardNumberVisible,
                    onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                    onCardLongClick = { onNavigateToCardDetail() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Member Card Section
            item {
                SectionHeader(
                    title = "Member Card", 
                    actionText = "Add Card",
                    onActionClick = onAddCard
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Stacked Cards - Member Cards
            item {
                StackedCardList(
                    cards = memberCards,
                    isCardNumberVisible = isCardNumberVisible,
                    onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                    onCardLongClick = { onNavigateToCardDetail() }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Electronic Money Card Section
            item {
                SectionHeader(
                    title = "Electronic Money Card", 
                    actionText = "Add Card",
                    onActionClick = onAddCard
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Stacked Cards - Electronic Money
            item {
                StackedCardList(
                    cards = electronicMoneyCards,
                    isCardNumberVisible = isCardNumberVisible,
                    onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                    onCardLongClick = { onNavigateToCardDetail() }
                )
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
        colors = CardDefaults.cardColors(containerColor = AppColors.BackgroundGray),
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
                tint = AppColors.PrimaryBlue,
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

@Preview(showBackground = true)
@Composable
fun CardsScreenPreview() {
    CardsScreen()
}
