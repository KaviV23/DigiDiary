package com.sunway.csc2074.digidiary.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.sunway.csc2074.digidiary.model.DiaryEntry
import com.sunway.csc2074.digidiary.viewmodel.DiaryEntryViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(navController: NavController) {
    val context = LocalContext.current

    var selDate = ""
    var selTime = ""

    val datePickerState = rememberUseCaseState()
    val timePickerState = rememberUseCaseState()

    var titleInputText by remember { mutableStateOf("") }
    var descInputText by remember { mutableStateOf("") }
    var dateBtnText by remember { mutableStateOf("Select Date") }
    var timeBtnText by remember { mutableStateOf("Select Time") }

    var imageUri by remember { mutableStateOf<Uri?>(null)}
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        })

    val diaryEntryViewModel: DiaryEntryViewModel = ViewModelProvider(context as ViewModelStoreOwner)[DiaryEntryViewModel::class.java]

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
                title = { Text(text = "Add Diary Entry", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (imageUri != null) {
                        imageUri?.let {
                            insertDataIntoDatabase(context, navController, diaryEntryViewModel, titleInputText, descInputText, selDate, selTime, it)
                        }
                    } else {
                        Toast.makeText(context, "Select an image first", Toast.LENGTH_SHORT).show()
                    }
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
            // Selected image
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .size(250.dp)
                )
            }
            // Select image button
            Button(onClick = {
                imagePicker.launch("image/*")
            }) {
                Text(text = "Select image")
            }

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
                    selTime = String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
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

private fun insertDataIntoDatabase(context : Context, navController: NavController, mDiaryEntryViewModel: DiaryEntryViewModel, title: String, description: String, date: String, time: String, uri: Uri) {
    if(inputCheck(title, description, date, time, uri)) {
        val savedImageUri = saveImage(context, uri)
        val entry = DiaryEntry(0, title, description, "$date $time", savedImageUri)
        mDiaryEntryViewModel.addEntry(entry)
        navController.popBackStack()
    } else {
        Toast.makeText(context, "Please provide all the information", Toast.LENGTH_SHORT).show()
    }

}

private fun saveImage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    var fileName = ""
    inputStream?.let {
        fileName = "${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        outputStream.close()
        inputStream.close()
    }
    return fileName
}

private fun inputCheck(title: String, description: String, date: String, time: String, uri: Uri): Boolean {
    return !(title.isBlank() || description.isBlank() || date.isBlank() || time.isBlank() || uri == null)
}