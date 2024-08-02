package com.sunway.csc2074.digidiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sunway.csc2074.digidiary.data.DiaryEntry
import com.sunway.csc2074.digidiary.data.DiaryEntryViewModel
import com.sunway.csc2074.digidiary.screen.SetupNavGraph
import com.sunway.csc2074.digidiary.ui.theme.DigiDiaryTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigiDiaryTheme {

                val diaryRepository = remember {
                    mutableListOf(
                        DiaryEntry(0, "Title", "Description", "DATE", "IMAGE"),
                        DiaryEntry(1, "Title2", "Description2", "DATE2", "IMAGE2")
                    )
                }

                navController = rememberNavController()
                SetupNavGraph(navController = navController, diaryEntries = diaryRepository, context = this)


            }

        }
    }
}