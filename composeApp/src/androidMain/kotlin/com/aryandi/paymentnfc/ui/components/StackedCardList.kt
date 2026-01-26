package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.zIndex
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

/**
 * Stacked Card List Component
 * Renders a list of cards in a vertical stack layout with pop-up animation on click.
 * All cards start at the bottom position by default. Click to pop up, click again to return.
 * 
 * @param cards List of CardData to display
 * @param stackOffset Vertical offset between stacked cards (default: 70.dp)
 * @param isCardNumberVisible Whether the card number is visible for the expanded card
 * @param onVisibilityToggle Callback to toggle card number visibility
 * @param onCardLongClick Callback when a card is long pressed (passes cardId for navigation)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StackedCardList(
    cards: List<CardData>,
    modifier: Modifier = Modifier,
    stackOffset: Dp = 70.dp,
    isCardNumberVisible: Boolean = false,
    isEditing: Boolean = false,
    onVisibilityToggle: () -> Unit = {},
    onCardLongClick: (cardId: String) -> Unit = {}
) {
    val cardHeight = 200.dp
    // Add padding to top to allow pop-up animation without clipping
    val popUpHeight = 12.dp
    val totalHeight = if (cards.isEmpty()) 0.dp else ((cards.size - 1) * stackOffset.value + cardHeight.value + popUpHeight.value).dp
    
    // Initialize with no card selected (all cards at bottom)
    var selectedCardIndex by remember(cards) { 
        mutableIntStateOf(-1) 
    }

    // Reset selection when entering edit mode
    LaunchedEffect(isEditing) {
        if (isEditing) {
            selectedCardIndex = -1
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
            .padding(top = popUpHeight) // Reserve space for pop-up
    ) {
        cards.forEachIndexed { index, card ->
            // Animate vertical position
            val isSelected = index == selectedCardIndex
            val targetOffsetY = if (isSelected) {
                (index * stackOffset.value).dp - popUpHeight
            } else {
                (index * stackOffset.value).dp
            }
            
            val animatedOffsetY by animateDpAsState(
                targetValue = targetOffsetY,
                animationSpec = spring()
            )
            
            // Calculate z-index to ensure correct stacking order
            // Match Home Screen: Last card (highest index) is on top
            val baseZIndex = index.toFloat()
            val zIndex = if (isSelected) cards.size.toFloat() + 1f else baseZIndex

            Box(
                modifier = Modifier
                    .offset(y = animatedOffsetY)
                    .zIndex(zIndex) 
                    .combinedClickable(
                        enabled = !isEditing,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Disable ripple for cleaner custom animation
                        onClick = {
                            selectedCardIndex = if (selectedCardIndex == index) -1 else index
                        },
                        onLongClick = {
                            onCardLongClick(card.id)
                        }
                    )
            ) {
                CreditCard(
                    cardData = card,
                    isVisible = isCardNumberVisible,
                    isExpanded = card.isExpanded || isSelected,
                    onVisibilityToggle = onVisibilityToggle
                )
                
                if (isEditing) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    )
                }
            }
        }
    }
}
