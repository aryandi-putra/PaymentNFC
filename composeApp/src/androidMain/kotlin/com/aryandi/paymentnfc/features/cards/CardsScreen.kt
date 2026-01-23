package com.aryandi.paymentnfc.features.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.aryandi.paymentnfc.presentation.viewmodel.CardsIntent
import com.aryandi.paymentnfc.presentation.viewmodel.CategoryWithCards
import com.aryandi.paymentnfc.ui.components.AddCategoryBottomSheet
import com.aryandi.paymentnfc.ui.components.AppBottomNavBar
import com.aryandi.paymentnfc.ui.components.BottomNavTab
import com.aryandi.paymentnfc.ui.components.CategoryCreatedDialog
import com.aryandi.paymentnfc.ui.components.EmptyCardState
import com.aryandi.paymentnfc.ui.components.HowToAddCardMenu
import com.aryandi.paymentnfc.ui.components.SectionHeader
import com.aryandi.paymentnfc.ui.components.StackedCardList
import com.aryandi.paymentnfc.ui.mapper.CardMapper
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

/**
 * Cards Screen - Dynamic categories implementation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    onBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCardDetail: (cardId: String) -> Unit = {},
    onAddCard: (categoryId: String) -> Unit = {},
    viewModel: CardsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isCardNumberVisible by remember { mutableStateOf(true) }
    
    // Add Category Bottom Sheet
    AddCategoryBottomSheet(
        isVisible = uiState.isAddCategorySheetVisible,
        onDismiss = { viewModel.onIntent(CardsIntent.HideAddCategorySheet) },
        onSave = { categoryName -> viewModel.onIntent(CardsIntent.AddCategory(categoryName)) },
        isLoading = uiState.isAddingCategory
    )
    
    // Category Created Success Dialog
    CategoryCreatedDialog(
        isVisible = uiState.showCategoryCreatedDialog,
        onDismiss = { viewModel.onIntent(CardsIntent.DismissCategoryCreatedDialog) }
    )
    
    // Check if there are any cards saved at all
    val hasAnyCards = remember(uiState.categoriesWithCards) {
        uiState.categoriesWithCards.any { it.cards.isNotEmpty() }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    if (uiState.isEditing) {
                        TextButton(onClick = { viewModel.onIntent(CardsIntent.ToggleEditMode) }) {
                            Text(
                                text = "Cancel",
                                color = AppColors.TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                },
                actions = {
                    if (uiState.isEditing) {
                        TextButton(onClick = { viewModel.onIntent(CardsIntent.ToggleEditMode) }) {
                            Text(
                                text = "Done",
                                color = AppColors.TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        TextButton(onClick = { viewModel.onIntent(CardsIntent.ToggleEditMode) }) {
                            Text(
                                text = "Edit",
                                color = AppColors.PrimaryBlue,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        bottomBar = { 
            AppBottomNavBar(
                selectedTab = BottomNavTab.CARDS,
                onHomeClick = onNavigateToHome
            ) 
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundWhite)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            // Header
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
                        onClick = { 
                            // Default to first category if available
                            uiState.categoriesWithCards.firstOrNull()?.category?.id?.let { 
                                onAddCard(it) 
                            }
                        }
                    )
                    ActionCard(
                        icon = Icons.Default.Category,
                        label = "Add Categories",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onIntent(CardsIntent.ShowAddCategorySheet) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // If no cards are saved, show "How to add card" menu
            if (!uiState.isLoading && !hasAnyCards) {
                item {
                    HowToAddCardMenu(
                        onDebitCreditClick = { 
                            // Default to debit_credit category if it exists
                            uiState.categoriesWithCards.find { 
                                it.category.id == "debit_credit" 
                            }?.category?.id?.let { onAddCard(it) } ?: onAddCard("debit_credit")
                        },
                        onOthersClick = { 
                            // Default to first non-debit_credit category
                            uiState.categoriesWithCards.find { 
                                it.category.id != "debit_credit" 
                            }?.category?.id?.let { onAddCard(it) } ?: onAddCard("member_card")
                        },
                        onGotItClick = { /* No-op or dismiss onboarding */ }
                    )
                }
            }
            
            // Loading State
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.PrimaryBlue)
                    }
                }
            }
            
            // Dynamic Categories with Cards - show all categories, empty ones show dashed onboarding box
            items(
                items = uiState.categoriesWithCards,
                key = { it.category.id }
            ) { categoryWithCards ->
                CategorySection(
                    categoryWithCards = categoryWithCards,
                    isCardNumberVisible = isCardNumberVisible,
                    isEditing = uiState.isEditing,
                    onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                    onAddCardClick = { onAddCard(categoryWithCards.category.id) },
                    onDeleteCategoryClick = { 
                        viewModel.onIntent(CardsIntent.DeleteCategory(categoryWithCards.category.id)) 
                    },
                    onCardClick = { cardId -> onNavigateToCardDetail(cardId) }
                )
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun CategorySection(
    categoryWithCards: CategoryWithCards,
    isCardNumberVisible: Boolean,
    isEditing: Boolean,
    onVisibilityToggle: () -> Unit,
    onAddCardClick: () -> Unit,
    onDeleteCategoryClick: () -> Unit,
    onCardClick: (cardId: String) -> Unit
) {
    val cardDataList = remember(categoryWithCards.cards) {
        CardMapper.toCardDataList(categoryWithCards.cards).map { 
            it.copy(isExpanded = true) 
        }
    }
    
    // Section Header
    SectionHeader(
        title = categoryWithCards.category.displayName,
        actionText = "Add Card",
        isEditing = isEditing,
        onActionClick = onAddCardClick,
        onDeleteClick = onDeleteCategoryClick
    )
    Spacer(modifier = Modifier.height(16.dp))
    
    // Show empty state or cards
    if (cardDataList.isEmpty()) {
        EmptyCardState(
            modifier = Modifier.padding(horizontal = 0.dp),
            onAddCardClick = onAddCardClick
        )
    } else {
        // Stacked Cards
        StackedCardList(
            cards = cardDataList,
            isCardNumberVisible = isCardNumberVisible,
            isEditing = isEditing,
            onVisibilityToggle = onVisibilityToggle,
            onCardLongClick = { cardId -> onCardClick(cardId) }
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
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
