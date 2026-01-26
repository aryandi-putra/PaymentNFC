package com.aryandi.paymentnfc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.aryandi.paymentnfc.ui.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview

/**
 * Base App Text Field with common styling
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isOutlined: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color = if (isOutlined) Color.White else AppColors.BackgroundLightPurple
) {
    if (isOutlined) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = AppColors.BorderLight,
                focusedBorderColor = AppColors.PrimaryBlueDark,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor
            )
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = Color.Gray) },
            singleLine = true,
            enabled = enabled,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledContainerColor = containerColor
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Filled Text Field with rounded corners - Used in Login Screen
 */
@Composable
fun FilledRoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        isOutlined = false,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

/**
 * Outlined Text Field with rounded corners - Used in Register Screen
 */
@Composable
fun OutlinedRoundedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        isOutlined = true,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FilledRoundedTextField(value = "", onValueChange = {}, placeholder = "Filled Text Field")
        OutlinedRoundedTextField(value = "", onValueChange = {}, placeholder = "Outlined Text Field")
    }
}
