package com.example.viewpaggerdemo.repository.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.viewpaggerdemo.model.local.*

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Transaction
    @Query("SELECT * FROM table_book")
    fun observerBook(): LiveData<List<Book>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageViewPager(imagePager: ImagePager)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGridViewImage(gridViewImage: GridViewImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageListViewImage(listViewImage: ListViewImage)

    @Transaction
    @Query("SELECT * FROM table_image_pager LIMIT :pageSize OFFSET :pageIndex * :pageSize")
    abstract suspend fun observerImageViewPager(pageSize: Int, pageIndex: Int):List<ImagePager>?

    @Transaction
    @Query("SELECT * FROM table_grid_image LIMIT :pageSize OFFSET :pageIndex * :pageSize")
    abstract suspend fun observerImageGridView(pageSize: Int, pageIndex: Int): List<GridViewImage>?

    @Transaction
    @Query("SELECT * FROM table_list_image LIMIT :pageSize OFFSET :pageIndex * :pageSize")
    abstract suspend fun observerImageListView(pageSize: Int, pageIndex: Int): List<ListViewImage>?


    @Query("SELECT * FROM table_list_image LIMIT :pageSize OFFSET :pageIndex * :pageSize")
    fun pagingSource(pageSize: Int, pageIndex: Int): PagingSource<Int, ImagePager>

     @Transaction
     @Query("SELECT * FROM table_image_pager")
     fun observerImageFromLocal(): PagingSource<Int, ImagePager>

     @Transaction
     @Query("SELECT * FROM table_grid_image")
     fun observerImageGridViewFromLocal(): PagingSource<Int, GridViewImage>

     @Transaction
     @Query("SELECT * FROM table_list_image")
     fun observerListViewImageFromLocal(): PagingSource<Int, ListViewImage>

     @Query("DELETE From table_image_pager")
     suspend fun deleteImagePager()

     @Query("DELETE From table_grid_image")
     suspend fun deleteGridImage()

     @Query("DELETE From table_list_image")
     suspend fun deleteListImage()


}