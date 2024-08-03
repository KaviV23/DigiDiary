package com.sunway.csc2074.digidiary.screen

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object AddScreen: Screen("add_screen")
    object UpdateScreen: Screen("update_screen")
}