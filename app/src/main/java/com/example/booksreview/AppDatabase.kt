package com.example.booksreview

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booksreview.dao.HistoryDao
import com.example.booksreview.dao.ReviewDao
import com.example.booksreview.model.History
import com.example.booksreview.model.Review

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}