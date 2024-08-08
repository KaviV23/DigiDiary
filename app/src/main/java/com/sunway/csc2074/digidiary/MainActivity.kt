package com.sunway.csc2074.digidiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sunway.csc2074.digidiary.screen.SetupNavGraph
import com.sunway.csc2074.digidiary.ui.theme.DigiDiaryTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DigiDiaryTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}