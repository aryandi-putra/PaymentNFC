package com.aryandi.paymentnfc.features.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LandingPageScreen(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit = {},
) {
    // Full‑screen vertical gradient background
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3C8CE7), // light blue
                        Color(0xFF5753C9), // indigo
                        Color(0xFF8E54E9), // purple
                    ),
                ),
            ),
    ) {
        // Main card content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Card illustration placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .height(260.dp)
                        .clip(RoundedCornerShape(32.dp)),
                    color = Color.Transparent,
                    shadowElevation = 0.dp,
                ) {
                    // Gradient card similar to the mockup
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF4FACFE),
                                        Color(0xFF00F2FE),
                                    ),
                                ),
                            ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Text and CTA area
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Jane Cooper",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ut enim ad minima veniam, quis nostrum exercita ionem ullam corporis suscipit laboriosam,",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE4E6FF),
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Circular button aligned to the end
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF3C8CE7),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continue",
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LandingPageScreenPreview() {
    LandingPageScreen()
}