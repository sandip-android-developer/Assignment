package com.example.viewpaggerdemo.view.ui.assigment1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpaggerdemo.R
import com.example.viewpaggerdemo.databinding.ItemBookListBinding
import com.example.viewpaggerdemo.model.responsepojo.BookListResponse


class Assignment1Adapter(
    private val context: Context,
    private val bookList: MutableList<BookListResponse.Result.BookDetail>
) :
    RecyclerView.Adapter<Assignment1Adapter.VhBookList>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Assignment1Adapter.VhBookList {
        val bindingViewpager: ItemBookListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_book_list,
            parent,
            false
        )
        return VhBookList(bindingViewpager)

    }

    class VhBookList(private val bindingBookList: ItemBookListBinding) :
        RecyclerView.ViewHolder(bindingBookList.root) {
        fun bind(bookDetails: BookListResponse.Result.BookDetail?) {
            bindingBookList.details = bookDetails
            bindingBookList.executePendingBindings()
        }

    }


    override fun onBindViewHolder(holder: VhBookList, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}