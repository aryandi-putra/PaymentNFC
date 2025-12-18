package com.aryandi.paymentnfc.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    
    @Serializable
    data object Landing : Screen
    
    @Serializable
    data object SignIn : Screen
    
    @Serializable
    data object SignUp : Screen
    
    @Serializable
    data class Home(val userId: String) : Screen
}
