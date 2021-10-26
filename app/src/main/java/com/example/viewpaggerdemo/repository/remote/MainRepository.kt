package com.example.viewpaggerdemo.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingSource
import com.example.viewpaggerdemo.api.ApiService
import com.example.viewpaggerdemo.model.local.*
import com.example.viewpaggerdemo.repository.local.AppDao
import com.example.viewpaggerdemo.repository.local.AppDatabase
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val dao: AppDao
) {
    suspend fun getCatImage(limit: Int, page: Int) =
        apiService.getCatImage(limit = limit, page = page)

    suspend fun bookList() = apiService.bookList()

    suspend fun insertImageViewPager(imagePager: ImagePager) {
        dao.insertImageViewPager(imagePager)
    }

    suspend fun insertGridViewImage(gridViewImage: GridViewImage) {
        dao.insertGridViewImage(gridViewImage)
    }

    suspend fun insertImageListViewImage(listViewImage: ListViewImage) {
        dao.insertImageListViewImage(listViewImage)
    }

    suspend fun insertBook(book: Book) {
        dao.insertBook(book)
    }

    suspend  fun observerImageViewPager(pageSize: Int, pageIndex: Int): List<ImagePager>? {
        return dao.observerImageViewPager(pageSize, pageIndex)
    }

    suspend fun observerImageGridView(pageSize: Int, pageIndex: Int):List<GridViewImage>? {
        return dao.observerImageGridView(pageSize, pageIndex)
    }

    suspend  fun observerImageListView(pageSize: Int, pageIndex: Int):List<ListViewImage>? {
        return dao.observerImageListView(pageSize, pageIndex)
    }

    fun observerBook(): LiveData<List<Book>>? {
        return dao.observerBook()
    }

}