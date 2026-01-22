package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aryandi.paymentnfc.ui.theme.AppColors

@Composable
fun FilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(Color(0xFF3A8375), RoundedCornerShape(12.dp))
            .size(56.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(18.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(2.dp)
                    .background(Color.White)
            )
        }
    }
}
