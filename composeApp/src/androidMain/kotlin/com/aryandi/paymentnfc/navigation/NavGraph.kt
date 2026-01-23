package com.aryandi.paymentnfc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aryandi.paymentnfc.features.addcard.AddCardTypeScreen
import com.aryandi.paymentnfc.features.addcard.AddDebitCreditCardScreen
import com.aryandi.paymentnfc.features.addcard.AddOthersCardScreen
import com.aryandi.paymentnfc.features.cards.CardDetailScreen
import com.aryandi.paymentnfc.features.cards.CardsScreen
import com.aryandi.paymentnfc.features.home.HomeV2Screen
import com.aryandi.paymentnfc.features.landing.LandingPageScreen
import com.aryandi.paymentnfc.features.login.SignInScreen
import com.aryandi.paymentnfc.features.otp.OtpScreen
import com.aryandi.paymentnfc.features.register.SignUpScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen = Screen.Home(userId = "test"),
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
            HomeV2Screen(
                onNavigateToCards = {
                    navController.navigate(Screen.Cards)
                },
                onNavigateToCardDetail = { cardId ->
                    navController.navigate(Screen.CardDetail(cardId = cardId))
                },
                onAddCard = { categoryId ->
                    // TODO: Pass categoryId to AddCard screen
                    navController.navigate(Screen.AddCardType)
                }
            )
            /* 
            // Previous Home V1 Implementation
            HomeScreen(
                userId = home.userId,
                onBack = {
                    navController.navigate(Screen.Landing) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                },
                onNavigateToCards = {
                    navController.navigate(Screen.Cards)
                }
            )
            */
        }
        
        composable<Screen.Cards> {
            CardsScreen(
                onBack = {
                    navController.navigateUp()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home(userId = "test"))
                },
                onNavigateToCardDetail = { cardId ->
                    navController.navigate(Screen.CardDetail(cardId = cardId))
                },
                onAddCard = { categoryId ->
                    // TODO: Pass categoryId to AddCard screen
                    navController.navigate(Screen.AddCardType)
                }
            )
        }

        composable<Screen.CardDetail> { backStackEntry ->
            val cardDetail = backStackEntry.toRoute<Screen.CardDetail>()
            CardDetailScreen(
                cardId = cardDetail.cardId,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        
        composable<Screen.AddCardType> {
            AddCardTypeScreen(
                onClose = {
                    navController.navigateUp()
                },
                onSelectDebitCredit = {
                    navController.navigate(Screen.AddDebitCreditCard)
                },
                onSelectOthers = {
                    navController.navigate(Screen.AddOthersCard)
                }
            )
        }
        
        composable<Screen.AddDebitCreditCard> {
            AddDebitCreditCardScreen(
                onBack = {
                    navController.navigateUp()
                },
                onSaveSuccess = {
                    navController.navigate(Screen.Cards) {
                        popUpTo(Screen.Cards) { inclusive = true }
                    }
                }
            )
        }
        
        composable<Screen.AddOthersCard> {
            AddOthersCardScreen(
                onBack = {
                    navController.navigateUp()
                },
                onSaveSuccess = {
                    navController.navigate(Screen.Cards) {
                        popUpTo(Screen.Cards) { inclusive = true }
                    }
                }
            )
        }
    }
}
