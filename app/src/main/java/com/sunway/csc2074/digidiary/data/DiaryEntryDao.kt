package com.sunway.csc2074.digidiary.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sunway.csc2074.digidiary.model.DiaryEntry

@Dao
interface DiaryEntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEntry(entry: DiaryEntry)

    @Update
    suspend fun updateEntry(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY id DESC")
    fun readAllData(): LiveData<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries WHERE id = :entryId")
    fun getEntryById(entryId: Int): LiveData<DiaryEntry>

    @Delete
    fun deleteEntry(entry: DiaryEntry)
}