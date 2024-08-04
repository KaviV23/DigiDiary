package com.sunway.csc2074.digidiary.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.sunway.csc2074.digidiary.model.DiaryEntry
import com.sunway.csc2074.digidiary.viewmodel.DiaryEntryViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(entryId: Int, navController: NavController) {
    val context = LocalContext.current

    val diaryEntryViewModel: DiaryEntryViewModel = ViewModelProvider(context as ViewModelStoreOwner)[DiaryEntryViewModel::class.java]
    val entry by diaryEntryViewModel.getEntryById(entryId).observeAsState()

    var selDate = ""
    var selTime = ""

    val datePickerState = rememberUseCaseState()
    val timePickerState = rememberUseCaseState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    var titleInputText by remember { mutableStateOf("") }
    var descInputText by remember { mutableStateOf("") }
    var dateBtnText by remember { mutableStateOf("") }
    var timeBtnText by remember { mutableStateOf("") }

    entry?.let {
        selDate = DateTimeExtractor.extractDate(it.dateTime)
        selTime = DateTimeExtractor.extractTime(it.dateTime)

        titleInputText = it.title
        descInputText = it.description
        dateBtnText = DateTimeExtractor.extractDate(it.dateTime)
        timeBtnText = DateTimeExtractor.extractTime(it.dateTime)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back to home screen")
                    }
                },
                title = { Text(text = "Update Diary Entry") },
                actions = {
                    IconButton(onClick = {
                        openDeleteDialog = true
                    }) {
                        Icon(Icons.Default.Delete, "Delete diary entry")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    updateData(context, navController, diaryEntryViewModel, entryId, titleInputText, descInputText, selDate, selTime)
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Update")
            }
        }
    ) { padding ->
        if (openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { openDeleteDialog = false },
                title = { Text(text = "Delete this entry?") },
                text = { Text(text = "This action will permanently delete this diary entry") },
                dismissButton = { Button(onClick = { openDeleteDialog = false }) { Text(text = "Cancel") }},
                confirmButton = {
                    Button(
                        onClick = {
                            entry?.let {
                                deleteData(navController, diaryEntryViewModel, it)
                            }
                            openDeleteDialog = false
                        }
                    ) {
                        Text(text = "Delete")
                    }
                })
        }
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
                label = { Text(text = "Update Title") }
            )

            // Description selector
            OutlinedTextField(
                value = descInputText,
                onValueChange = { descInputText = it },
                label = { Text(text = "Update Description") }
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

private fun deleteData(navController: NavController, diaryEntryViewModel: DiaryEntryViewModel, entry: DiaryEntry) {
    diaryEntryViewModel.deleteEntry(entry)
    navController.popBackStack()
}

private fun updateData(context: Context, navController: NavController, diaryEntryViewModel: DiaryEntryViewModel, entryId: Int, title: String, description: String, date: String, time: String) {
    if (inputCheck(title, description, date, time)) {
        // Create diary entry object
        val updatedEntry = DiaryEntry(entryId, title, description, "$date $time", "IMAGE")
        // Update current entry
        diaryEntryViewModel.updateEntry(updatedEntry)
        // Navigate back home
        navController.popBackStack()
    } else {
        Toast.makeText(context, "Please provide all the information", Toast.LENGTH_SHORT).show()
    }
}

private fun inputCheck(title: String, description: String, date: String, time: String): Boolean {
    return !(title.isBlank() || description.isBlank() || date.isBlank() || time.isBlank())
}

private object DateTimeExtractor {
    private var dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun extractDate(dateTime: String): String {
        val date = dateTimeFormat.parse(dateTime)
        return dateFormat.format(date!!)
    }

    fun extractTime(dateTime: String): String {
        val date = dateTimeFormat.parse(dateTime)
        return timeFormat.format(date!!)
    }
}