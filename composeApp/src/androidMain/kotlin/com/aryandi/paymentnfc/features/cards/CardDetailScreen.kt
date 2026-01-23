package com.aryandi.paymentnfc.features.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.domain.model.CardTypeModel
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.presentation.viewmodel.CardDetailEvent
import com.aryandi.paymentnfc.presentation.viewmodel.CardDetailIntent
import com.aryandi.paymentnfc.ui.components.CardData
import com.aryandi.paymentnfc.ui.components.CardType
import com.aryandi.paymentnfc.ui.components.DetailedCreditCard
import com.aryandi.paymentnfc.ui.components.DeleteCardDialog
import com.aryandi.paymentnfc.ui.components.FilledRoundedTextField
import com.aryandi.paymentnfc.ui.components.FilterButton
import com.aryandi.paymentnfc.ui.components.MoreActionsBottomSheet
import com.aryandi.paymentnfc.ui.components.SetDefaultPaymentDialog
import com.aryandi.paymentnfc.ui.components.SuccessDialog
import com.aryandi.paymentnfc.ui.components.TransactionItem
import com.aryandi.paymentnfc.ui.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import com.aryandi.paymentnfc.presentation.viewmodel.CardDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    onBack: () -> Unit = {},
    viewModel: CardDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Load card data on first composition
    LaunchedEffect(cardId) {
        viewModel.onIntent(CardDetailIntent.LoadCard(cardId))
    }

    // Map domain Card to UI CardData
    val cardData = remember(uiState.card) {
        uiState.card?.let { card ->
            CardData(
                bankName = card.bankName,
                cardType = when (card.cardType) {
                    CardTypeModel.VISA -> CardType.VISA
                    CardTypeModel.MASTERCARD -> CardType.MASTERCARD
                },
                backgroundColor = try {
                    Color(android.graphics.Color.parseColor(card.colorHex))
                } catch (e: Exception) {
                    AppColors.CardOlive
                },
                cardHolder = card.cardHolder,
                cardNumber = card.cardNumber,
                maskedNumber = card.maskedNumber
            )
        } ?: CardData(
            bankName = "Loading...",
            cardType = CardType.VISA,
            backgroundColor = AppColors.CardOlive,
            cardHolder = ""
        )
    }
    
    // Dialog states
    var showMoreActionsSheet by remember { mutableStateOf(false) }
    var showSetDefaultPaymentDialog by remember { mutableStateOf(false) }
    var showDefaultPaymentSuccessDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessDialog by remember { mutableStateOf(false) }
    
    // Handle ViewModel events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                CardDetailEvent.DefaultPaymentSet -> {
                    showSetDefaultPaymentDialog = false
                    showDefaultPaymentSuccessDialog = true
                }
                CardDetailEvent.CardDeleted -> {
                    showDeleteDialog = false
                    showDeleteSuccessDialog = true
                }
                is CardDetailEvent.Error -> {
                    // Could show a snackbar here
                }
            }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMoreActionsSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "More Actions",
                            tint = AppColors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = AppColors.BackgroundWhite
                )
            )
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(600.dp) // Give it enough height for scrolling
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        FilledRoundedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = "Search",
                            trailingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    FilterButton(onClick = {})
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    uiState.transactions.forEach { (date, transactions) ->
                        item {
                            Text(
                                text = date,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AppColors.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        items(transactions) { transaction ->
                            TransactionItem(transaction)
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 350.dp,
        sheetContainerColor = AppColors.BackgroundWhite,
        sheetContentColor = AppColors.TextPrimary,
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        sheetShadowElevation = 10.dp,
        containerColor = AppColors.BackgroundWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Card Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Card Details",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                DetailedCreditCard(cardData = cardData)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedButton(
                    onClick = { /* TODO: Implement show card details functionality */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Show Card Details",
                        color = Color(0xFF3A8375),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // More Actions Bottom Sheet
    MoreActionsBottomSheet(
        isVisible = showMoreActionsSheet,
        onDismiss = { showMoreActionsSheet = false },
        onSetDefaultPayment = { 
            showMoreActionsSheet = false
            showSetDefaultPaymentDialog = true 
        },
        onDeleteCard = { 
            showMoreActionsSheet = false
            showDeleteDialog = true 
        }
    )
    
    // Set Default Payment Confirmation Dialog
    SetDefaultPaymentDialog(
        isVisible = showSetDefaultPaymentDialog,
        onConfirm = {
            viewModel.onIntent(CardDetailIntent.SetDefaultPayment)
        },
        onCancel = { showSetDefaultPaymentDialog = false }
    )
    
    // Success Dialog (Default Payment Updated)
    if (showDefaultPaymentSuccessDialog) {
        SuccessDialog(
            title = "Default Payment Updated",
            description = "Your payment default has been updated successfully.",
            onDismiss = { showDefaultPaymentSuccessDialog = false }
        )
    }

    // Delete Card Confirmation Dialog
    if (showDeleteDialog) {
        DeleteCardDialog(
            onDelete = { 
                viewModel.onIntent(CardDetailIntent.DeleteCard)
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
    
    // Success Dialog (Card Deleted)
    if (showDeleteSuccessDialog) {
        SuccessDialog(
            title = "The Card Has Been Deleted",
            description = "The card has been successfully deleted and is no longer active.",
            onDismiss = { 
                showDeleteSuccessDialog = false
                onBack()
            }
        )
    }
}

@Preview
@Composable
fun CardDetailScreenPreview() {
    // Note: Preview won't work with koinViewModel injection without Koin context
    // Ideally we'd wrap this or provide a fake VM, but for now just commenting out
    // or we can use a separate Composable for the content that takes state as params
}
