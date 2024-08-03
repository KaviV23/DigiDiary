package com.sunway.csc2074.digidiary.repository

import androidx.lifecycle.LiveData
import com.sunway.csc2074.digidiary.data.DiaryEntryDao
import com.sunway.csc2074.digidiary.model.DiaryEntry

class DiaryEntryRepository(private val diaryEntryDao: DiaryEntryDao) {

    val readALlData: LiveData<List<DiaryEntry>> = diaryEntryDao.readAllData()

    suspend fun addEntry(entry: DiaryEntry) {
        diaryEntryDao.addEntry(entry)
    }
}