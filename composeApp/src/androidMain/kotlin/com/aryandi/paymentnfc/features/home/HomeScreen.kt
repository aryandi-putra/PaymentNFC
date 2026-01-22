package com.aryandi.paymentnfc.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.presentation.viewmodel.HomeViewModel
import com.aryandi.paymentnfc.ui.components.AppBottomNavBar
import com.aryandi.paymentnfc.ui.components.BottomNavTab
import com.aryandi.paymentnfc.ui.components.SectionHeaderWithViewAll
import com.aryandi.paymentnfc.ui.components.SimpleCreditCard
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Home Screen - Implementation based on design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userId: String = "",
    onBack: () -> Unit = {},
    onNavigateToCards: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(parameters = { parametersOf(userId) })
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { 
            AppBottomNavBar(
                selectedTab = BottomNavTab.HOME,
                onAiClick = onNavigateToCards
            ) 
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.BackgroundWhite)
                .padding(paddingValues)
        ) {
            // Blue Background for Top Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(AppColors.PrimaryBlue)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    HomeHeader(
                        name = uiState.userName,
                        userId = uiState.userId
                    )
                }
                
                item {
                    QuickActionsSection()
                }
                
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                
                item {
                    CardsSection(count = uiState.cardsCount)
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                item {
                    // Transactions Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                            .background(AppColors.BackgroundWhite)
                            .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
                    ) {
                        SectionHeaderWithViewAll(
                            title = "Recent transactions",
                            onViewAllClick = { }
                        )
                    }
                }

                if (uiState.isLoading && uiState.transactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AppColors.PrimaryBlue)
                        }
                    }
                }

                items(uiState.transactions) { transaction ->
                    Box(modifier = Modifier.background(AppColors.BackgroundWhite).padding(horizontal = 20.dp)) {
                        TransactionItem(transaction)
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp)) // Extra space for bottom bar
                }
            }
        }
    }
}

@Composable
fun HomeHeader(name: String, userId: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Yellow) // Placeholder for image
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                if (userId.isNotEmpty()) {
                    Text(
                        text = "ID: $userId",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }
        
        // Notification Icon with Badge
        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            // Badge
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
            )
        }
    }
}

@Composable
fun QuickActionsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Quick actions",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(icon = Icons.Default.SwapHoriz, label = "Transfer")
            QuickActionButton(icon = Icons.Default.Star, label = "Add a template")
            QuickActionButton(icon = Icons.Default.IosShare, label = "Share")
        }
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CardsSection(count: Int) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Cards and accounts ($count)",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        val pagerState = rememberPagerState(pageCount = { count })
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(end = 32.dp),
                pageSpacing = 16.dp
            ) { page ->
                SimpleCreditCard()
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Pager Indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(count) { index ->
                    val color = if (pagerState.currentPage == index) AppColors.PrimaryBlue else Color.LightGray
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(AppColors.BackgroundGray, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            val icon = when(transaction.title) {
                "Currency exchange" -> Icons.Default.CurrencyExchange
                "Cash-in" -> Icons.Default.AccountBalanceWallet
                "Cashback from purchase" -> Icons.Default.ShoppingCart
                else -> Icons.Default.CreditCard
            }
            Icon(imageVector = icon, contentDescription = null, tint = AppColors.PrimaryBlue)
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = transaction.date, fontSize = 12.sp, color = Color.Gray)
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(text = transaction.amount, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            StatusBadge(transaction.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val bgColor = if (status == "pending") AppColors.PendingBg else AppColors.ConfirmedBg
    val textColor = if (status == "pending") AppColors.PendingText else AppColors.ConfirmedText
    
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(text = status, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        userId = "12345",
        onBack = {},
        onNavigateToCards = {}
    )
}