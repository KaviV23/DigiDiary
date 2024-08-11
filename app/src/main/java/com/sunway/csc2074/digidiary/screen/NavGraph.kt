package com.sunway.csc2074.digidiary.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.AddScreen.route
        ) {
            AddScreen(navController)
        }
        composable(
            route = Screen.UpdateScreen.route,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType } )
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getInt("entryId") ?: return@composable
            UpdateScreen(entryId, navController)
        }
    }
}