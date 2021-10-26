package com.example.viewpaggerdemo.view.ui.assigment1

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewpaggerdemo.R
import com.example.viewpaggerdemo.databinding.ActivityAssignment1Binding
import com.example.viewpaggerdemo.model.responsepojo.BookListResponse
import com.example.viewpaggerdemo.util.CommonUtils
import com.example.viewpaggerdemo.util.Status
import com.example.viewpaggerdemo.view.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Assignment1Activity : BaseActivity() {
    private var resultList: MutableList<BookListResponse.Result> = mutableListOf()
    private val bookViewModel: Assignment1ViewModel by viewModels()
    private val bookList: MutableList<BookListResponse.Result.BookDetail> =
        mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private val bindingBookItem by lazy {
        DataBindingUtil.setContentView<ActivityAssignment1Binding>(
            this,
            R.layout.activity_assignment_1
        )
    }

    private fun initView() {
        recyclerViewSetup()
        bookListApiObserver()
    }


    private fun recyclerViewSetup() {
        bindingBookItem.rvBookList.apply {
            layoutManager = LinearLayoutManager(this@Assignment1Activity)
            adapter = Assignment1Adapter(this@Assignment1Activity, bookList)
        }
    }

    private fun bookListApiObserver() {
        if (CommonUtils.isConnected(this)) {
            bookViewModel.res.observe(this, Observer { it ->
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            println("data--${it.message}")
                            hideProgress()
                            resource.data?.let { book ->
                                if (book.status == "OK") {
                                    resultList =
                                        book.results as MutableList<BookListResponse.Result>
                                    bookList.clear()
                                    val bookDetail: BookListResponse.Result.BookDetail =
                                        BookListResponse.Result.BookDetail()
                                    resultList.forEach {

                                        it.bookDetails?.forEach { book ->
                                            bookDetail.apply {
                                                bookDetail.ageGroup = book.ageGroup
                                                bookDetail.author = book.author
                                                bookDetail.contributor = book.contributor

                                                bookDetail.contributorNote = book.contributorNote
                                                bookDetail.description = book.description
                                                bookDetail.price = book.price
                                                bookDetail.primaryIsbn1 = book.primaryIsbn1
                                                bookDetail.primaryIsbn13 = book.primaryIsbn13
                                                bookDetail.publisher = book.publisher
                                                bookDetail.title = book.title
                                                bookList.add(bookDetail)
                                            }
                                        }


                                    }

                                    bindingBookItem.rvBookList.adapter =
                                        Assignment1Adapter(this@Assignment1Activity, bookList)
                                } else {
                                    Snackbar.make(
                                        bindingBookItem.rootView,
                                        "Something went wrong",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        Status.ERROR -> {
                            println("data--${it.message}")
                            hideProgress()
                        }
                        Status.LOADING -> {
                            println("data--${it.message}")
                            showProgress(this)
                        }
                    }

                }
            })
        } else {
            bookViewModel.resLocal.observe(this, Observer { it ->
                it?.let { resource ->
                    println("data--1--${resource.data}")
                    when (resource.status) {
                        Status.SUCCESS -> {
                            println("data--1--${resource.data}")
                            //hideProgress()
                            resource.data?.let { book ->
                                bookList.clear()
                                bookList.addAll(book)
                                bindingBookItem.rvBookList.adapter =
                                    Assignment1Adapter(this@Assignment1Activity, bookList)
                            }
                        }
                        Status.ERROR -> {
                            println("data--${it.message}")
                            //  hideProgress()
                        }
                        Status.LOADING -> {
                            println("data--${it.message}")
                            //showProgress(this)
                        }
                    }

                }
            })
        }

    }
}