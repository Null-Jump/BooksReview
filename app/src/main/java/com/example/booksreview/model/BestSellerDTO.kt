package com.example.booksreview.model

import com.google.gson.annotations.SerializedName

/**
 * 전체 모델에서 특정 모델을 꺼내올 수 있게 하는 데이터 클래스
 * BestSeller 모델 안에있는 item 모델을 꺼내도록 함
 */
data class BestSellerDTO(
    @SerializedName("title") val title:String,
    @SerializedName("item") val books:List<Book> // BestSeller 모델에서 item 모델
)
