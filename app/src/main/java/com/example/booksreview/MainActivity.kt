package com.example.booksreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.booksreview.adapter.BookAdapter
import com.example.booksreview.adapter.HistoryAdapter
import com.example.booksreview.api.BookService
import com.example.booksreview.databinding.ActivityMainBinding
import com.example.booksreview.model.BestSellerDTO
import com.example.booksreview.model.Book
import com.example.booksreview.model.History
import com.example.booksreview.model.SearchBookDto
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
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interParkAPIKey))
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

    private fun search(keyword: String){
        bookService.getBooksByName(getString(R.string.interParkAPIKey), keyword)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    hideHistoryRecyclerView()
                    saveSearchKeyword(keyword)

                    // 성공처리
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT SUCCESS")
                        return
                    }
                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    // 실패처리
                    hideHistoryRecyclerView()
                    Log.e(TAG, t.toString())
                }
            })

    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView(){
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

        initSearchEditText()
    }

    private fun initSearchEditText(){
        binding.searchEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN ){
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true // setOnKeyListener 가 실행되엇을때 해당 조건문의 조건에 충족하면 조건문을 처리하고 완료
            }
            return@setOnKeyListener false // setOnKeyListener 가 실행되었을때 조건문에 해당하지 않을 때는 시스템이 정의한 이벤트로 처리
        }

        binding.searchEditText.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN){
                showHistoryRecyclerView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryRecyclerView(){
        Thread{ //DB 에서 데이터를 가져온다음에 어댑터에 넣어서 보여주기때문에 메인쓰레드가 아닌 서브쓰레드로 처리해야함
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryRecyclerView(){
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) { //history DB 안에 검색한 키워드를 저장하는 메소드
        Thread{ // 메인쓰레드가 아닌 새로운 쓰레드를 통해 입력한 키워드를 저장해야함 (메인쓰레드는 검색한 뒤 검색 결과를 보여줘야함)
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String){
        Thread{
            db.historyDao().delete(keyword)
            showHistoryRecyclerView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}