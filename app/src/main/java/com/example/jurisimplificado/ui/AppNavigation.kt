package com.example.jurisimplificado.ui

import ChatScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jurisimplificado.ui.forgotpassword.ForgotPasswordScreen
import com.example.jurisimplificado.ui.login.LoginScreen
import com.example.jurisimplificado.ui.register.RegisterScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password"
    const val CHAT = "chat"

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val startDestination = if (Firebase.auth.currentUser != null) {
        AppRoutes.CHAT
    } else {
        AppRoutes.LOGIN
    }

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH){

        composable(AppRoutes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(startDestination) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.LOGIN){
            LoginScreen (
                onLoginSuccess = {
                    navController.navigate(AppRoutes.CHAT) {
                        popUpTo(AppRoutes.LOGIN) {inclusive =
                        true}
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN)
                }

            )
        }
        composable (AppRoutes.REGISTER){
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) {inclusive = true}
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(AppRoutes.CHAT){
            ChatScreen(
                onLogout = {
                    Firebase.auth.signOut()
                    navController.navigate(AppRoutes.LOGIN){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD_SCREEN){
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}