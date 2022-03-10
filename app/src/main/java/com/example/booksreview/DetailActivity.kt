package com.example.booksreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.booksreview.databinding.ActivityDetailBinding
import com.example.booksreview.model.Book
import com.example.booksreview.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")

        // 인텐트에서 넘어온 모델이 null 일 수 있기 때문에 nullable 하게 해야하므로 model?. 을 사용
        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread { // 클릭해서 해당 아이템을 불러올때 기존에 저장한 내용이 있다면 그 내용을 불러오기 위한 쓰레드
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0) // DB 작업을 할때는 항상 null 일 수 있다는걸 생각하고 처리할 필요가 있음
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty()) // 리뷰를 작성하지 않은 아이템(EditText 의 내용이 null 인 경우)을 불러올때를 위한 nullable 처리
            }
        }.start()

        // saveButton 을 클릭했을때 EditText 의 내용을 저장하는 이벤트 처리와 이벤트를 위한 쓰레드
        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }
}