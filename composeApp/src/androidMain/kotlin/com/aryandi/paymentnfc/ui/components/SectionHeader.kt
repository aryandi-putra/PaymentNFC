package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview
import com.aryandi.paymentnfc.R
import androidx.compose.ui.res.stringResource

/**
 * Unified Section Header Component with optional actions
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
    isEditing: Boolean = false,
    onDeleteClick: () -> Unit = {},
    showViewAll: Boolean = false,
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
            color = AppColors.TextPrimary
        )
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isEditing) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            } else if (actionText != null) {
                TextButton(
                    onClick = onActionClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = AppColors.PrimaryBlue
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = actionText,
                        fontSize = 14.sp,
                        color = AppColors.PrimaryBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else if (showViewAll) {
                TextButton(onClick = onViewAllClick) {
                    Text(
                        text = stringResource(R.string.show_more),
                        color = AppColors.PrimaryBlue,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(title = "Categories", actionText = "Add Card")
        SectionHeader(title = "Recent Transactions", showViewAll = true)
        SectionHeader(title = "Editing Category", isEditing = true)
    }
}
