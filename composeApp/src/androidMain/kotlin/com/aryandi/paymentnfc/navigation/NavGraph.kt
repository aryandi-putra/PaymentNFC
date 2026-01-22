package com.aryandi.paymentnfc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aryandi.paymentnfc.features.cards.CardsScreen
import com.aryandi.paymentnfc.features.home.HomeScreen
import com.aryandi.paymentnfc.features.landing.LandingPageScreen
import com.aryandi.paymentnfc.features.login.SignInScreen
import com.aryandi.paymentnfc.features.otp.OtpScreen
import com.aryandi.paymentnfc.features.register.SignUpScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen = Screen.Landing,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Screen.Landing> {
            LandingPageScreen(
                onContinue = {
                    navController.navigate(Screen.SignIn)
                },
            )
        }
        
        composable<Screen.SignIn> {
            SignInScreen(
                onBack = {
                    navController.navigateUp()
                },
                onSignInSuccess = { username ->
                    navController.navigate(Screen.Otp(emailOrPhone = username))
                },
                onSignUp = {
                    navController.navigate(Screen.SignUp)
                },
                onForgotPassword = { username ->
                    navController.navigate(Screen.Otp(emailOrPhone = username.ifBlank { "User" }))
                }
            )
        }
        
        composable<Screen.SignUp> {
            SignUpScreen(
                onBack = {
                    navController.navigateUp()
                },
                onSignUpSuccess = { username ->
                    navController.navigate(Screen.Otp(emailOrPhone = username))
                },
                onSignIn = {
                    navController.navigate(Screen.SignIn) {
                        popUpTo(Screen.SignUp) { inclusive = true }
                    }
                }
            )
        }
        
        composable<Screen.Otp> { backStackEntry ->
            val otp = backStackEntry.toRoute<Screen.Otp>()
            OtpScreen(
                emailOrPhone = otp.emailOrPhone,
                onBack = {
                    navController.navigateUp()
                },
                onVerifySuccess = { userId ->
                    navController.navigate(Screen.Home(userId = userId)) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Home> { backStackEntry ->
            val home = backStackEntry.toRoute<Screen.Home>()
            HomeScreen(
                userId = home.userId,
                onBack = {
                    navController.navigate(Screen.Landing) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                },
            )
        }
        
        composable<Screen.Cards> {
            CardsScreen(
                onBack = {
                    navController.navigateUp()
                },
                onEdit = {
                    // Handle edit action
                }
            )
        }
    }
}
