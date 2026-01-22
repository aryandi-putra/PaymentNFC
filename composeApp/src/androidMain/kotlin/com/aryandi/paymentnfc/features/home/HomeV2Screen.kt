package com.aryandi.paymentnfc.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.components.*
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeV2Screen(
    onNavigateToCards: () -> Unit = {},
    onNavigateToCardDetail: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    
    // Sample Data
    val retailCards = remember {
        listOf(
            CardData("AriaBank", CardType.VISA, AppColors.CardOrange),
            CardData("AriaBank", CardType.MASTERCARD, AppColors.CardYellow),
            CardData("WeBank", CardType.VISA, AppColors.CardOlive, "Alexander Parra", isExpanded = true)
        )
    }
    
    val memberCards = remember {
        listOf(
            CardData("Alfagift", CardType.MASTERCARD, AppColors.CardBrown),
            CardData("IKEA", CardType.VISA, AppColors.CardYellow),
            CardData("Starbucks", CardType.VISA, AppColors.CardDarkGreen, "Alexander Parra", isExpanded = true)
        )
    }
    
    val eMoneyCards = remember {
        listOf(
            CardData("BNI Tapcash", CardType.MASTERCARD, AppColors.CardOrange),
            CardData("Mandiri E-Money", CardType.VISA, AppColors.CardNavy),
            CardData("Flazz BCA", CardType.MASTERCARD, AppColors.CardBlue, "Alexander Parra", isExpanded = true)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ariapay",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TextPrimary // Or a specific teal color if needed
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
                    
                    SectionHeaderWithViewAll(
                        title = title,
                        modifier = Modifier.padding(bottom = 16.dp),
                        onViewAllClick = { } // Show More action
                    )
                    
                    val cards = when(page) {
                        0 -> retailCards
                        1 -> memberCards
                        else -> eMoneyCards
                    }
                    
                    var isCardNumberVisible by remember { mutableStateOf(false) }
                    
                    StackedCardList(
                        cards = cards,
                        stackOffset = 100.dp,
                        isCardNumberVisible = isCardNumberVisible,
                        onVisibilityToggle = { isCardNumberVisible = !isCardNumberVisible },
                        onCardLongClick = { onNavigateToCardDetail() }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Pager Indicators
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

@Composable
fun SectionHeaderWithViewAll(
    title: String,
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.TextPrimary
        )
        TextButton(onClick = onViewAllClick) {
            Text(
                text = "Show More", 
                color = AppColors.PrimaryBlue,
                fontSize = 14.sp
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun HomeV2ScreenPreview() {
    HomeV2Screen()
}
