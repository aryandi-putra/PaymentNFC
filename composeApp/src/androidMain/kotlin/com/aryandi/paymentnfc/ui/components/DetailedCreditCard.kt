package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailedCreditCard(
    cardData: CardData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardData.backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Row: Bank Name and Visa Logo
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
                    
                    Text(
                        text = "VISA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Balance
                Text(
                    text = "$•••••",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Card Number
                Text(
                    text = "•••• •••• •••• ••••",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bottom Row: Date, Holder, CVV
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Date •• ••",
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
                        text = "CVV •••",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = 24.dp) // Align with Date
                    )
                }
            }
        }
    }
}
