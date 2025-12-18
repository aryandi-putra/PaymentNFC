package com.aryandi.paymentnfc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.aryandi.paymentnfc.home.HomeScreen
import com.aryandi.paymentnfc.landing.LandingPageScreen
import com.aryandi.paymentnfc.signin.SignInScreen
import com.aryandi.paymentnfc.signup.SignUpScreen

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
                onSignInSuccess = { userId ->
                    navController.navigate(Screen.Home(userId = userId)) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                },
                onSignUp = {
                    navController.navigate(Screen.SignUp)
                },
            )
        }
        
        composable<Screen.SignUp> {
            SignUpScreen(
                onBack = {
                    navController.navigateUp()
                },
                onSignUp = { name, email, password ->
                    navController.navigate(Screen.Home(userId = email)) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                },
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
    }
}
