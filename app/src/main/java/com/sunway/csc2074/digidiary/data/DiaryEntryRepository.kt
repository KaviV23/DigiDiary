package com.sunway.csc2074.digidiary.data

import androidx.lifecycle.LiveData

class DiaryEntryRepository(private val diaryEntryDao: DiaryEntryDao) {

    val readALlData: LiveData<List<DiaryEntry>> = diaryEntryDao.readAllData()

    suspend fun addEntry(entry: DiaryEntry) {
        diaryEntryDao.addEntry(entry)
    }
}