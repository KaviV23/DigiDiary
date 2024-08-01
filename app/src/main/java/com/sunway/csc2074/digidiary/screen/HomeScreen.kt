package com.sunway.csc2074.digidiary.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sunway.csc2074.digidiary.R
import com.sunway.csc2074.digidiary.data.DiaryEntry
import com.sunway.csc2074.digidiary.ui.theme.DigiDiaryTheme

@Composable
fun HomeScreen(navController: NavController, diaryEntries: MutableList<DiaryEntry>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                .fillMaxSize()
        ) {
            items(diaryEntries.size) { index ->
                ListDiaryEntry(diaryEntries[index])
            }
        }
    }
}

@Composable
fun ListDiaryEntry(entry: DiaryEntry) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = entry.title)
            Text(text = entry.dateTime)
        }
        Column {
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription ="Diary image")
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), diaryEntries = mutableListOf(DiaryEntry(0,"poo","poo", "TIME")))
}