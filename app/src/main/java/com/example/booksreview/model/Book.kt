package com.example.booksreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 인터파크에서 데이터값을 가져오고 저장할 모델 클래스
 * @SerializedName() -> API 에서 가져올 값과 data class 에서 사용할 값을 매핑해서 사용할 수 있게하는 오버라이드
 * @Parcelize -> 직렬화를 시켜서 오브젝트인 Book data class 를 한번에 보낼 수 있게 해줌
 */

@Parcelize
data class Book(
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
): Parcelable
