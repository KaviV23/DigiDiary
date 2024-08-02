package com.sunway.csc2074.digidiary.screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.sunway.csc2074.digidiary.data.DiaryEntry
import com.sunway.csc2074.digidiary.data.DiaryEntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(context: ComponentActivity, navController: NavController) {
    var selDate = ""
    var selTime = ""

    val datePickerState = rememberUseCaseState()
    val timePickerState = rememberUseCaseState()

    var titleInputText by remember { mutableStateOf("") }
    var descInputText by remember { mutableStateOf("") }
    var dateBtnText by remember { mutableStateOf("Select Date") }
    var timeBtnText by remember { mutableStateOf("Select Time") }

    val mDiaryEntryViewModel: DiaryEntryViewModel = ViewModelProvider(context)[DiaryEntryViewModel::class.java]

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back to homescreen")
                    }
                },
                title = { Text(text = "Add Diary Entry") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    insertDataIntoDatabase(navController, mDiaryEntryViewModel, titleInputText, descInputText, selDate, selTime)
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title selector
            OutlinedTextField(
                value = titleInputText,
                onValueChange = { titleInputText = it },
                label = {Text(text = "Enter Title")}
            )

            // Description selector
            OutlinedTextField(
                value = descInputText,
                onValueChange = { descInputText = it },
                label = {Text(text = "Enter Description")}
            )

            // Date selector
            CalendarDialog(
                state = datePickerState,
                config = CalendarConfig(
                    monthSelection = true,
                    yearSelection = true
                ),
                selection = CalendarSelection.Date { date ->
                    selDate = "$date"
                    dateBtnText = "Date: $selDate"
                }
            )
            Button(onClick = {
                datePickerState.show()
            }) {
                Text(text = dateBtnText)
            }

            // Time selector
            ClockDialog(
                state = timePickerState,
                config = ClockConfig(
                    is24HourFormat = true,
                ),
                selection = ClockSelection.HoursMinutes { hours, minutes ->
                    selTime = "$hours:$minutes"
                    timeBtnText = "Time: $selTime"
                }
            )
            Button(onClick = {
                timePickerState.show()
            }) {
                Text(text = timeBtnText)
            }
        }
    }
}

private fun insertDataIntoDatabase(navController: NavController, mDiaryEntryViewModel: DiaryEntryViewModel, title: String, description: String, date: String, time: String) {
    if(inputCheck(title, description, date, time)) {
        // Create diary entry object
        val entry = DiaryEntry(0, title, description, "$date $time", "IMAGE")
        mDiaryEntryViewModel.addEntry(entry)
        Log.d("DATABASE", "Success")
        navController.popBackStack()
    } else {
        Log.d("DATABASE", "Failure")
    }

}

private fun inputCheck(title: String, description: String, date: String, time: String): Boolean {
    return !(title.isBlank() || description.isBlank() || date.isBlank() || time.isBlank())
}

//@Composable
//@Preview(showBackground = true)
//fun AddScreenPreview() {
//    AddScreen()
//}