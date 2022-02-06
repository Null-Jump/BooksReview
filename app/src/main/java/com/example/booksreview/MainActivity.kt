package com.example.booksreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksreview.adapter.BookAdapter
import com.example.booksreview.api.BookService
import com.example.booksreview.databinding.ActivityMainBinding
import com.example.booksreview.model.BestSellerDTO
import com.example.booksreview.model.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 여기서 사용할 기능들
 * 1. RecyclerView - 리스트형식의 View
 * 2. View Binding - findById를 사용하지 않고 XML을 연결하는 API
 * 3. Retrofit - 인터넷 통신 API
 * 4. Glide - 이미지 처리를 위한 API
 * 5. Android Room - 내장 DB를 사용할 수 있게 하는 API
 * 6. Open API - 인터파크 도서 API 를 사용할 예정
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("3AF8890DCA35C7A1180FF586421BD0F954F180FD8288D234AA7CA2EB0B5F8E1E")
            .enqueue(object : Callback<BestSellerDTO> {
                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    // 성공처리
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        // BestSellerDTO 안에 book 이 따로 있기때문에 한번 더 로그를 찍음
                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }

                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    // 실패처리
                    Log.e(TAG, t.toString())
                }
            })
    }

    fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}