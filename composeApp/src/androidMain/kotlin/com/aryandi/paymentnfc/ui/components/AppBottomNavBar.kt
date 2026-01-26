package com.aryandi.paymentnfc.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.aryandi.paymentnfc.R

/**
 * Navigation item data class
 */
data class NavItem(
    val label: String,
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit = {}
)

/**
 * Bottom Navigation Tab enum for tracking selected state
 */
enum class BottomNavTab {
    HOME, CARDS, AI, PROFILE
}

/**
 * Shared Bottom Navigation Bar Component
 */
@Composable
fun AppBottomNavBar(
    selectedTab: BottomNavTab = BottomNavTab.HOME,
    onHomeClick: () -> Unit = {},
    onCardsClick: () -> Unit = {},
    onAiClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val navItems = listOf(
        NavItem(
            label = stringResource(R.string.home),
            icon = Icons.Default.Home,
            contentDescription = stringResource(R.string.home),
            onClick = onHomeClick
        ),
        NavItem(
            label = stringResource(R.string.cards),
            icon = Icons.Default.CreditCard,
            contentDescription = stringResource(R.string.cards),
            onClick = onCardsClick
        ),
        NavItem(
            label = stringResource(R.string.ai),
            icon = Icons.Default.SmartToy,
            contentDescription = stringResource(R.string.ai),
            onClick = onAiClick
        ),
        NavItem(
            label = stringResource(R.string.profile),
            icon = Icons.Default.Person,
            contentDescription = stringResource(R.string.profile),
            onClick = onProfileClick
        )
    )

    NavigationBar(
        containerColor = AppColors.BackgroundWhite,
        tonalElevation = 8.dp
    ) {
        navItems.forEachIndexed { index, item ->
            val isSelected = when (index) {
                0 -> selectedTab == BottomNavTab.HOME
                1 -> selectedTab == BottomNavTab.CARDS
                2 -> selectedTab == BottomNavTab.AI
                3 -> selectedTab == BottomNavTab.PROFILE
                else -> false
            }
            
            NavigationBarItem(
                selected = isSelected,
                onClick = item.onClick,
                icon = { Icon(item.icon, contentDescription = item.contentDescription) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.PrimaryBlue,
                    selectedTextColor = AppColors.PrimaryBlue
                )
            )
        }
    }
}
