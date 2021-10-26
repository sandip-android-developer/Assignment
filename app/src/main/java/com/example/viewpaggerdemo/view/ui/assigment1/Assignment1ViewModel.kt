package com.example.viewpaggerdemo.view.ui.assigment1

import android.content.Context
import androidx.lifecycle.*
import com.example.viewpaggerdemo.core.ImageLoadingApplication
import com.example.viewpaggerdemo.model.local.Book
import com.example.viewpaggerdemo.model.responsepojo.BookListResponse
import com.example.viewpaggerdemo.repository.remote.MainRepository
import com.example.viewpaggerdemo.util.CommonUtils
import com.example.viewpaggerdemo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Assignment1ViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {
    private lateinit var owner: LifecycleOwner
    /* fun getBook(
         activity: Assignment1Activity
     ) = liveData(Dispatchers.IO) {
         emit(Resource.loading(data = null))
         try {
             emit(
                 Resource.success(
                     data = getBookData(
                         activity
                     )
                 )
             )
         } catch (exceptions: UnknownHostException) {
             runBlocking(Dispatchers.Main) {
                 Toast.makeText(activity, "Unkown host ", Toast.LENGTH_SHORT)

             }
             emit(
                 Resource.error(
                     data = null,
                     msg = "Unkown host "
                 )

             )
         } catch (exceptions: ConnectException) {
             runBlocking(Dispatchers.Main) {
                 Toast.makeText(activity, "Connection error", Toast.LENGTH_SHORT)
             }
             emit(
                 Resource.error(
                     data = null,
                     msg = "Connection error"
                 )

             )
         } catch (exceptions: SocketTimeoutException) {
             runBlocking(Dispatchers.Main) {
                 Toast.makeText(activity, "Session Timeout", Toast.LENGTH_SHORT)
             }
             emit(
                 Resource.error(
                     data = null,
                     msg = "Session Timeout"
                 )

             )
         } catch (e: Exception) {
             emit(Resource.error(data = null, msg = e.message ?: "Error Occur in image"))
         }
     }


     private suspend fun getBookData(
         activity: Assignment1Activity,
     ): Response<BookListResponse> {
         return withContext(Dispatchers.IO) {
             print("Activity--${activity.componentName}")
             repository.bookList()
         }

     }*/

    private val _res = MutableLiveData<Resource<BookListResponse>>()
    val res: LiveData<Resource<BookListResponse>>
        get() = _res

    private val _resLocal =
        MutableLiveData<Resource<MutableList<BookListResponse.Result.BookDetail>>>()
    val resLocal: LiveData<Resource<MutableList<BookListResponse.Result.BookDetail>>>
        get() = _resLocal

    init {
        if (CommonUtils.isConnected(ImageLoadingApplication.applicationContext())) {
            getBook()
        }else{
            getBookFromLocal()
        }
    }

    private fun getBook() = viewModelScope.launch {
        _res.postValue(Resource.loading(null))
        repository.bookList().let {
            if (it.isSuccessful) {
                it.body()!!.results?.forEach { result ->
                    result.bookDetails?.forEach { details ->
                        repository.insertBook(
                            Book(
                                title = details.title ?: "",
                                description = details.description ?: "",
                                contributor = details.contributor ?: "",
                                author = details.author ?: "",
                                price = details.price ?: 0,
                                publisher = details.publisher ?: "",
                            )
                        )
                    }

                }

                _res.postValue(Resource.success(it.body()))
            } else {
                _res.postValue(Resource.error(it.errorBody().toString(), null))
            }
        }
    }

    private fun getBookFromLocal() = viewModelScope.launch {
        _resLocal.postValue(Resource.loading(null))
        val bookList: MutableList<BookListResponse.Result.BookDetail> =
            mutableListOf()
        repository.observerBook()?.observeForever(Observer {
            it?.forEach {

                val bookDetail: BookListResponse.Result.BookDetail =
                    BookListResponse.Result.BookDetail()
                bookDetail.apply {
                    bookDetail.ageGroup = ""
                    bookDetail.author = it.author
                    bookDetail.contributor = it.contributor

                    bookDetail.contributorNote = ""
                    bookDetail.description = it.description
                    bookDetail.price = it.price
                    bookDetail.primaryIsbn1 = ""
                    bookDetail.primaryIsbn13 = ""
                    bookDetail.publisher = it.publisher
                    bookDetail.title = it.title
                    bookList.add(bookDetail)
                }
            }
            _resLocal.postValue(Resource.success(bookList))
        })


    }
}