package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Direct Stacked Card List Component
 * Renders a list of cards in a vertical stack layout.
 * Does NOT have pop-up animation. Clicking a card navigates directly.
 * 
 * @param cards List of CardData to display
 * @param stackOffset Vertical offset between stacked cards (default: 70.dp)
 * @param isCardNumberVisible Whether the card number is visible for the expanded card
 * @param onVisibilityToggle Callback to toggle card number visibility
 * @param onCardClick Callback when a card is clicked (navigate to detail)
 */
@Composable
fun DirectStackedCardList(
    cards: List<CardData>,
    modifier: Modifier = Modifier,
    stackOffset: Dp = 70.dp,
    isCardNumberVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {},
    onCardClick: (CardData) -> Unit = {}
) {
    val cardHeight = 200.dp
    val totalHeight = if (cards.isEmpty()) 0.dp else ((cards.size - 1) * stackOffset.value + cardHeight.value).dp
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        cards.forEachIndexed { index, card ->
            val offsetY = (index * stackOffset.value).dp
            
            Box(
                modifier = Modifier
                    .offset(y = offsetY)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // Disable default ripple if desired, or keep it
                        onClick = {
                            onCardClick(card)
                        }
                    )
            ) {
                CreditCard(
                    cardData = card,
                    isVisible = isCardNumberVisible,
                    onVisibilityToggle = onVisibilityToggle
                )
            }
        }
    }
}
