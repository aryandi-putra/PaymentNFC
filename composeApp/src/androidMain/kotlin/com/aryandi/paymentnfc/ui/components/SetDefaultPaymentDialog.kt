package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.ui.res.stringResource
import com.aryandi.paymentnfc.R
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Confirmation dialog for setting a card as default payment
 * Shows card illustration and confirmation buttons
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDefaultPaymentDialog(
    isVisible: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onCancel,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = AppColors.BackgroundWhite,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            dragHandle = {
                Spacer(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3D-style Card Illustration
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Back Card (Salmon/Reddish)
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 65.dp)
                            .offset(x = (-10).dp, y = (-15).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8A598)) // Light salmon
                    )
                    
                    // Front Card (Teal)
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 70.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF4DB6AC)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Chip detail simulation
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 12.dp)
                                .size(width = 20.dp, height = 15.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFFFFD54F).copy(alpha = 0.6f))
                        )
                        
                        // Card network logo placeholder
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .size(width = 30.dp, height = 20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.set_as_default_payment),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.set_default_confirmation),
                    fontSize = 16.sp,
                    color = AppColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Continue Button
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3A8375) // Teal color from design
                    )
                ) {
                    Text(
                        text = stringResource(R.string.continue_text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cancel Button
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE57373) // Red text
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun SetDefaultPaymentDialogPreview() {
    SetDefaultPaymentDialog(
        isVisible = true,
        onConfirm = {},
        onCancel = {}
    )
}
