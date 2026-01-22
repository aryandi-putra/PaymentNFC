package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.ui.theme.AppColors

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
