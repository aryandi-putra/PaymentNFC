package com.aryandi.paymentnfc

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aryandi.paymentnfc.landing.LandingPageScreen
import com.aryandi.paymentnfc.signin.SignInScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showSignIn by remember { mutableStateOf(false) }

        if (showSignIn) {
            SignInScreen(
                onBack = { showSignIn = false },
                onSignIn = { _, _ -> },
                onSignUp = { /* TODO: navigate to sign up */ },
            )
        } else {
            LandingPageScreen(
                onContinue = { showSignIn = true },
            )
        }
    }
}