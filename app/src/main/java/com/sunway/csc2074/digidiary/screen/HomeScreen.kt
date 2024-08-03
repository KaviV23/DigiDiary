package com.sunway.csc2074.digidiary.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.sunway.csc2074.digidiary.R
import com.sunway.csc2074.digidiary.model.DiaryEntry
import com.sunway.csc2074.digidiary.viewmodel.DiaryEntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(context: ComponentActivity, navController: NavController) {

    val diaryEntryViewModel: DiaryEntryViewModel = ViewModelProvider(context)[DiaryEntryViewModel::class.java]
    val diaryEntries by diaryEntryViewModel.readAllData.observeAsState(emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "DigiDiary")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddScreen.route)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add diary entry")
            }
        }
    ){ padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(diaryEntries.size) { index ->
                ListDiaryEntry(diaryEntries[index], navController)
            }
        }
    }
}

@Composable
fun ListDiaryEntry(entry: DiaryEntry, navController: NavController) {
    val padding = 10.dp
    Row(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable {
                navController.navigate(Screen.UpdateScreen.createRoute(entry.id))
            }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(padding)
        ) {
            Text(
                text = entry.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = entry.dateTime,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription ="Diary image")
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun HomeScreenPreview() {
//    HomeScreen(navController = rememberNavController(), diaryEntries = mutableListOf(
//        DiaryEntry(0,"poo","poo", "TIME"),
//        DiaryEntry(0,"poo","poo", "TIME")
//    ))
//}