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
            label = "Home",
            icon = Icons.Default.Home,
            contentDescription = "Home",
            onClick = onHomeClick
        ),
        NavItem(
            label = "Cards",
            icon = Icons.Default.CreditCard,
            contentDescription = "Cards",
            onClick = onCardsClick
        ),
        NavItem(
            label = "AI",
            icon = Icons.Default.SmartToy,
            contentDescription = "AI",
            onClick = onAiClick
        ),
        NavItem(
            label = "Profile",
            icon = Icons.Default.Person,
            contentDescription = "Profile",
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
