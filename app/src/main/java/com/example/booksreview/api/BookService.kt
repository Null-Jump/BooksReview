package com.example.booksreview.api

import com.example.booksreview.model.BestSellerDTO
import com.example.booksreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 인터페이스는 구현할 것에 대한 정의만 할 뿐 실제로 실행되지는 않음
 * 따라서, 레트로핏에서 create 를 통해 실행 할 수 있음
 */
interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?&categoryId=100&output=json")
    fun getBestSellerBooks(
        @Query("key") apiKey: String,
    ): Call<BestSellerDTO>
}