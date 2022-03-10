package com.example.booksreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.booksreview.databinding.ItemBookBinding
import com.example.booksreview.model.Book

/**
 * RecyclerView 를 사용하기 위한 Adapter Class
 *
 */
class BookAdapter(private val itemClickedListener: (Book) -> Unit) : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    // layout 에 item_book.xml 의 이름에 맞춰서 ItemBookBinding 으로 이름을 설정하면 databinding 으로 임포트 가능
    inner class BookItemViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description

            binding.root.setOnClickListener { // binding.root 를 클릭했을때 itemClickedListener()가 호출됨
                itemClickedListener(bookModel)
            }

            Glide.with(binding.coverImageView.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)
        }
    }

    // 미리 만들어진 ViewHolder 가 없을 경우에 새로 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // viewHolder 가 view 에 그려졌을때 데이터를 바인드 해주는 함수
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        // 데이터를 새로고침 할때 기존의 데이터와 같은 데이터가 있다면 굳이 데이터를 서버에서 한번 더 가져올 필요가 없도록 하기 위한 판단근거를 정의하는 변수
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            // areItemsTheSame -> 가져온 아이템 자체가 같은지 다른지 확인
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            // areContentsTheSame -> 가져온 아이템안에 담긴 컨텐츠가 같은지 다른지 확인
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}