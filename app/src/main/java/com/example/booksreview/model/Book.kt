package com.example.booksreview.model

import com.google.gson.annotations.SerializedName

/**
 * 인터파크에서 데이터값을 가져오고 저장할 모델 클래스
 * @SerializedName() -> API 에서 가져올 값과 data class 에서 사용할 값을 매핑해서 사용할 수 있게하는 오버라이드
 */
data class Book(
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
)
