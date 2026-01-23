package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors

/**
 * Section Header Component - Displays a title with optional action button
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    showAddIcon: Boolean = true,
    isEditing: Boolean = false,
    onActionClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        
        if (isEditing) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Category",
                        tint = AppColors.PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Category",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else if (actionText != null) {
            TextButton(onClick = onActionClick) {
                if (showAddIcon) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = AppColors.PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = actionText,
                    color = AppColors.PrimaryBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Section Header with View All action - Common pattern for list headers
 */
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        TextButton(onClick = onViewAllClick) {
            Text(text = "View all", color = Color.Gray)
        }
    }
}
