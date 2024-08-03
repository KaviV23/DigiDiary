package com.sunway.csc2074.digidiary.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryEntryViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<DiaryEntry>>
    private val repository: DiaryEntryRepository

    init {
        val diaryEntryDao = DiaryDatabase.getDatabase(application).diaryEntryDao()
        repository = DiaryEntryRepository(diaryEntryDao)
        readAllData = repository.readALlData
    }

    fun addEntry(entry: DiaryEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEntry(entry)
        }
    }


}