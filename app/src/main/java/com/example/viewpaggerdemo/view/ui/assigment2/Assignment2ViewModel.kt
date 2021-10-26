package com.example.viewpaggerdemo.view.ui.assigment2

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.viewpaggerdemo.model.local.GridViewImage
import com.example.viewpaggerdemo.model.local.ImagePager
import com.example.viewpaggerdemo.model.local.ListViewImage
import com.example.viewpaggerdemo.model.responsepojo.ImageResponse
import com.example.viewpaggerdemo.repository.remote.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class Assignment2ViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {
    fun imageList(
        activity: Assignment2Activity,
        type: Int,
        isOffline: Boolean
    ): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImagePagingSourceFromNetwork(
                    repository,
                    activity, type,
                )
            }
        )
            .flow.cachedIn(viewModelScope)


    }

    fun imageListFromDb(
        activity: Assignment2Activity,
        type: Int,
        isOffline: Boolean
    ): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {

                ImagePagingSourceFromDbList(
                    repository,
                    activity
                )
            }
        )
            .flow.cachedIn(viewModelScope)

    }

    fun imageListPagerFromDb(
        activity: Assignment2Activity,
        type: Int,
        isOffline: Boolean
    ): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {

                ImagePagingSourceFromDbPager(
                    repository,
                    activity
                )

            }
        )
            .flow.cachedIn(viewModelScope)

    }

    fun imageListGridFromDb(
        activity: Assignment2Activity,
        type: Int,
        isOffline: Boolean
    ): Flow<PagingData<ImageResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {

                ImagePagingSourceFromDbGrid(
                    repository,
                    activity
                )

            }
        )
            .flow.cachedIn(viewModelScope)

    }

    class ImagePagingSourceFromNetwork(
        private var service: MainRepository,
        private var activity: Assignment2Activity,
        val type: Int
    ) :
        PagingSource<Int, ImageResponse>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
            val position = params.key ?: 1
            println("snlnsn--")
            var response: Response<List<ImageResponse>>? = null
            var workerList: MutableList<ImageResponse> = mutableListOf()
            try {
                response = service.getCatImage(params.loadSize, position)
                if (response.code() == 401) {
                    Toast.makeText(activity, "Session expire", Toast.LENGTH_SHORT)
                }

                if (response.body()!! != null) {
                    workerList = (response.body() as MutableList<ImageResponse>?)!!
                    when (type) {
                        1 -> {
                            workerList.forEach { imageResponse ->
                                service.insertGridViewImage(
                                    GridViewImage(
                                        imageUrl = imageResponse.url ?: ""
                                    )
                                )
                            }
                        }
                        2 -> {
                            workerList.forEach { imageResponse ->
                                service.insertImageListViewImage(
                                    ListViewImage(
                                        imageUrl = imageResponse.url ?: ""
                                    )
                                )
                            }
                        }
                        3 -> {
                            workerList.forEach { imageResponse ->
                                service.insertImageViewPager(
                                    ImagePager(
                                        imageUrl = imageResponse.url ?: ""
                                    )
                                )
                            }
                        }
                    }
                }
                activity.hideProgress()
                return if (params.loadSize == workerList.size) {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (workerList.isEmpty()) null else position + 1
                    )
                } else {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = null
                    )
                }


            } catch (exception: UnknownHostException) {
                Toast.makeText(activity, "UnknownHostException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: ConnectException) {
                Toast.makeText(activity, "ConnectException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: SocketTimeoutException) {
                Toast.makeText(activity, "Server time out", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: IOException) {
                println("exception--1--" + exception.message)
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            } catch (exception: Exception) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }


    class ImagePagingSourceFromDbGrid(
        private var service: MainRepository,
        private var activity: Assignment2Activity
    ) :
        PagingSource<Int, ImageResponse>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
            val position = params.key ?: 1
            var response: Response<List<ImageResponse>>? = null
            var workerList: MutableList<ImageResponse> = mutableListOf()
            try {

                val data =
                    service.observerImageGridView(params.loadSize, position - 1)
                data?.forEach {
                    val image: ImageResponse = ImageResponse()
                    image.url = it.imageUrl
                    workerList.add(image)
                }
                activity.hideProgress()
                return if (params.loadSize == workerList.size) {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (workerList.isEmpty()) null else position + 1
                    )
                } else {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = null
                    )

                }

            } catch (exception: UnknownHostException) {
                Toast.makeText(activity, "UnknownHostException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: ConnectException) {
                Toast.makeText(activity, "ConnectException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: SocketTimeoutException) {
                Toast.makeText(activity, "Server time out", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: IOException) {
                println("exception--1--" + exception.message)
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            } catch (exception: Exception) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }

    class ImagePagingSourceFromDbList(
        private var service: MainRepository,
        private var activity: Assignment2Activity

    ) :
        PagingSource<Int, ImageResponse>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
            val position = params.key ?: 1
            var response: Response<List<ImageResponse>>? = null
            var workerList: MutableList<ImageResponse> = mutableListOf()
            try {
                val data =
                    service.observerImageListView(params.loadSize, position - 1)
                data?.forEach {
                    val image: ImageResponse = ImageResponse()
                    image.url = it.imageUrl
                    workerList.add(image)
                }
                activity.hideProgress()

                return if (params.loadSize == workerList.size) {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (workerList.isEmpty()) null else position + 1
                    )
                } else {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = null
                    )
                }


            } catch (exception: UnknownHostException) {
                Toast.makeText(activity, "UnknownHostException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: ConnectException) {
                Toast.makeText(activity, "ConnectException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: SocketTimeoutException) {
                Toast.makeText(activity, "Server time out", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: IOException) {
                println("exception--1--" + exception.message)
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                 activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            } catch (exception: Exception) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            }
        }


        override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }

    class ImagePagingSourceFromDbPager(
        private var service: MainRepository,
        private var activity: Assignment2Activity

    ) :
        PagingSource<Int, ImageResponse>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageResponse> {
            val position = params.key ?: 1
            val workerList: MutableList<ImageResponse> = mutableListOf()
            try {
                val data =
                    service.observerImageViewPager(params.loadSize, position - 1)
                data?.forEach {
                    val image: ImageResponse = ImageResponse()
                    image.url = it.imageUrl
                    workerList.add(image)

                }
                activity.hideProgress()
                return if (params.loadSize == workerList.size) {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (workerList.isEmpty()) null else position + 1
                    )
                } else {
                    LoadResult.Page(
                        data = workerList,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = null
                    )
                }


            } catch (exception: UnknownHostException) {
                Toast.makeText(activity, "UnknownHostException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: ConnectException) {
                Toast.makeText(activity, "ConnectException", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: SocketTimeoutException) {
                Toast.makeText(activity, "Server time out", Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: IOException) {
                println("exception--1--" + exception.message)
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                activity.hideProgress()
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            } catch (exception: Exception) {
                activity.hideProgress()
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT)
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, ImageResponse>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }
    }
}