package com.example.booksreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booksreview.dao.HistoryDao
import com.example.booksreview.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}