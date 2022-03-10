package com.example.booksreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booksreview.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review WHERE id == :id")
    fun getOneReview(id: Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 이미 기존에 저장되어 있는게 있다면 덮어씌움
    fun saveReview(review: Review)
}