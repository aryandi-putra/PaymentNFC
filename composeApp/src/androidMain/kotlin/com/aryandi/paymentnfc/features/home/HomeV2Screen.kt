package com.aryandi.paymentnfc.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.presentation.viewmodel.HomeV2ViewModel
import com.aryandi.paymentnfc.ui.components.*
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeV2Screen(
    onNavigateToCards: () -> Unit = {},
    onNavigateToCardDetail: (cardId: String) -> Unit = {},
    onAddCard: (categoryId: String) -> Unit = {},
    viewModel: HomeV2ViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Filter categories to only include those that have cards
    val categoriesWithCards = remember(uiState.categoriesWithCards) {
        uiState.categoriesWithCards.filter { it.cards.isNotEmpty() }
    }
    
    // Check if there are any cards at all across all categories
    val hasAnyCards = categoriesWithCards.isNotEmpty()
    
    // Dynamic page count based on non-empty categories
    val pageCount = maxOf(categoriesWithCards.size, 1)
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ariapay",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { 
                            // Add card to current category
                            categoriesWithCards.getOrNull(pagerState.currentPage)?.category?.id?.let {
                                onAddCard(it)
                            } ?: run {
                                // Default to first available category if none in pager
                                uiState.categoriesWithCards.firstOrNull()?.category?.id?.let { onAddCard(it) }
                            }
                        },
                        modifier = Modifier
                            .background(AppColors.TextPrimary, CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        bottomBar = {
            AppBottomNavBar(
                selectedTab = BottomNavTab.HOME,
                onCardsClick = onNavigateToCards
            )
        },
        containerColor = AppColors.BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AppColors.PrimaryBlue)
                }
            } else if (!hasAnyCards) {
                // Empty state when no cards exist across any category
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyCardState(
                        onAddCardClick = { 
                            // Default to first category or navigate to cards screen
                            val firstCategoryId = uiState.categoriesWithCards.firstOrNull()?.category?.id
                            if (firstCategoryId != null) onAddCard(firstCategoryId) else onNavigateToCards()
                        }
                    )
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    pageSpacing = 16.dp,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    val categoryWithCards = categoriesWithCards.getOrNull(page)
                    
                    Column {
                        SectionHeader(
                            title = categoryWithCards?.category?.displayName ?: "",
                            modifier = Modifier.padding(bottom = 16.dp),
                            showViewAll = true,
                            onViewAllClick = { onNavigateToCards() }
                        )
                        
                        val cards = categoryWithCards?.cards ?: emptyList()
                        
                        var isCardNumberVisible by remember { mutableStateOf(false) }
                        
                        DirectStackedCardList(
                            cards = cards,
                            stackOffset = 100.dp,
                            isCardNumberVisible = isCardNumberVisible,
                            onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                            onCardClick = { cardId -> onNavigateToCardDetail(cardId) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Pager Indicators - only show if there are multiple non-empty categories
            if (categoriesWithCards.size > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pageCount) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width = if (isSelected) 24.dp else 8.dp
                        val color = if (isSelected) AppColors.TextPrimary else AppColors.TextGray.copy(alpha = 0.3f)
                        
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .background(color, CircleShape)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun HomeV2ScreenPreview() {
    // Note: Preview won't show data as ViewModel requires Koin DI
    // Run the app to see actual data
    HomeV2Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeV2ScreenEmptyPreview() {
    // Static preview showing empty state
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ariapay",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .background(AppColors.TextPrimary, CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        bottomBar = {
            AppBottomNavBar(
                selectedTab = BottomNavTab.HOME,
                onCardsClick = { }
            )
        },
        containerColor = AppColors.BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                pageSpacing = 16.dp,
                verticalAlignment = Alignment.Top
            ) { page ->
                Column {
                    val title = when(page) {
                        0 -> "Retail & Shopping"
                        1 -> "Member Card"
                        else -> "Electronic Money Card"
                    }
                    
                    SectionHeader(
                        title = title,
                        modifier = Modifier.padding(bottom = 16.dp),
                        showViewAll = true,
                        onViewAllClick = { }
                    )
                    
                    EmptyCardState(
                        onAddCardClick = { }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pagerState.pageCount) { index ->
                    val isSelected = pagerState.currentPage == index
                    val width = if (isSelected) 24.dp else 8.dp
                    val color = if (isSelected) AppColors.TextPrimary else AppColors.TextGray.copy(alpha = 0.3f)
                    
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .background(color, CircleShape)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
