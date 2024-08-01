package com.sunway.csc2074.digidiary.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class DiaryDatabase: RoomDatabase() {
    abstract fun diaryEntryDao(): DiaryEntryDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}