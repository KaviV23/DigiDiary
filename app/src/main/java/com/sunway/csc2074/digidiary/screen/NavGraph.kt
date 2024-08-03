package com.sunway.csc2074.digidiary.screen

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.sunway.csc2074.digidiary.data.DiaryEntry
import com.sunway.csc2074.digidiary.data.DiaryEntryViewModel

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