package com.sunway.csc2074.digidiary.screen

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    context: ComponentActivity,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(context, navController)
        }
        composable(
            route = Screen.AddScreen.route
        ) {
            AddScreen(context, navController)
        }
    }
}