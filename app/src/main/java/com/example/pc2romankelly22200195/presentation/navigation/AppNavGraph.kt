package com.example.pc2romankelly22200195.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pc2romankelly22200195.data.remote.FirebaseAuthManager
import com.example.pc2romankelly22200195.presentation.auth.LoginScreen
import com.example.pc2romankelly22200195.presentation.conversion.ConversionScreen
import com.example.pc2romankelly22200195.presentation.history.HistoryScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val start = if (FirebaseAuthManager.isLoggedIn()) "conversion" else "login"

    NavHost(navController = navController, startDestination = start) {
        composable("login") { LoginScreen(navController) }
        composable("conversion") { ConversionScreen(navController) }
        composable("history") { HistoryScreen(navController) }
    }
}