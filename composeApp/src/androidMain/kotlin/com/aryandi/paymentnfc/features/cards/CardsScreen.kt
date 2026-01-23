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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.presentation.viewmodel.CardsViewModel
import com.aryandi.paymentnfc.ui.components.AppBottomNavBar
import com.aryandi.paymentnfc.ui.components.BottomNavTab
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.components.CardType
import com.aryandi.paymentnfc.ui.components.SectionHeader
import com.aryandi.paymentnfc.ui.components.StackedCardList
import com.aryandi.paymentnfc.ui.mapper.CardMapper
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

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
    onAddCard: () -> Unit = {},
    viewModel: CardsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCardNumberVisible by remember { mutableStateOf(true) }
    
    val debitCreditCards = remember(uiState.debitCreditCards) {
        CardMapper.toCardDataList(uiState.debitCreditCards).map { 
            it.copy(isExpanded = true) 
        }
    }
    
    val memberCards = remember(uiState.memberCards) {
        CardMapper.toCardDataList(uiState.memberCards).map { 
            it.copy(isExpanded = true)
        }
    }
    
    val electronicMoneyCards = remember(uiState.eMoneyCards) {
        CardMapper.toCardDataList(uiState.eMoneyCards).map { 
            it.copy(isExpanded = true)
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
