package com.sunway.csc2074.digidiary.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sunway.csc2074.digidiary.data.DiaryEntry

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    diaryEntries: MutableList<DiaryEntry>
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            HomeScreen(navController, diaryEntries)
        }
        composable(
            route = Screen.AddScreen.route
        ) {
            AddScreen()
        }
    }
}