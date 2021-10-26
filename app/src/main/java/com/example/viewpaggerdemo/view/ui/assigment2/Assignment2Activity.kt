package com.example.viewpaggerdemo.view.ui.assigment2


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.viewpaggerdemo.R
import com.example.viewpaggerdemo.databinding.ActivityAssignemt2Binding
import com.example.viewpaggerdemo.model.responsepojo.ImageResponse
import com.example.viewpaggerdemo.util.CommonUtils
import com.example.viewpaggerdemo.util.ProgressLoadStateAdapter
import com.example.viewpaggerdemo.view.base.BaseActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class Assignment2Activity : BaseActivity() {
    private lateinit var binding: ActivityAssignemt2Binding
    private var toolbar: MaterialToolbar? = null
    private var imageList: MutableList<ImageResponse> = mutableListOf()
    private val viewModel: Assignment2ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignemt2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        imageApi(3)

    }


    private fun imageApi(type: Int) {
        val imageAdapter = ViewPagerAdapter(this, type)
        when (type) {
            3 -> {
                binding.pager.isVisible = true
                binding.rvImage.isVisible = false
                binding.pager.adapter = imageAdapter
            }
            2 -> {
                binding.pager.isVisible = false
                binding.rvImage.isVisible = true
                binding.rvImage.apply {
                    layoutManager = LinearLayoutManager(this@Assignment2Activity)
                    adapter = imageAdapter.withLoadStateFooter(
                        footer = ProgressLoadStateAdapter { imageAdapter.retry() }
                    )
                }
            }
            1 -> {
                binding.pager.isVisible = false
                binding.rvImage.isVisible = true
                binding.rvImage.apply {
                    layoutManager = GridLayoutManager(this@Assignment2Activity, 2)
                    adapter = imageAdapter.withLoadStateFooter(
                        footer = ProgressLoadStateAdapter { imageAdapter.retry() }
                    )
                }
            }
        }

        if (CommonUtils.isConnected(this)) {
            imageAdapter.addLoadStateListener { loadState ->
                // show empty list
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && imageAdapter!!.itemCount == 0
                // showEmptyList(isListEmpty)
            }
            showProgress(this)
            lifecycleScope.launch {
                println("yehi aa rha hai--")
                viewModel.imageList(
                    this@Assignment2Activity, type, false
                )
                    .collectLatest { review ->
                        imageAdapter.submitData(review)
                    }
            }
        } else {
            when (type) {
                1 -> {
                    lifecycleScope.launch {
                        viewModel.imageListGridFromDb(
                            this@Assignment2Activity, type, true
                        )
                            .collectLatest { review ->
                                imageAdapter.submitData(review)
                            }

                    }
                }
                2 -> {
                    lifecycleScope.launch {
                        viewModel.imageListFromDb(
                            this@Assignment2Activity, type, true
                        )
                            .collectLatest { review ->
                                imageAdapter.submitData(review)
                            }
                    }
                }
                3 -> {
                    lifecycleScope.launch {
                        viewModel.imageListPagerFromDb(
                            this@Assignment2Activity, type, true
                        )
                            .collectLatest { review ->
                                imageAdapter.submitData(review)
                            }
                    }
                }
            }
        }


    }

    /*private fun showEmptyList(listEmpty: Boolean) {
        if (listEmpty) {
            bindingWorker?.txtNoDataFoundFragWorker?.text=getString(R.string.no_data_found)
            bindingWorker?.txtNoDataFoundFragWorker?.visibility = View.VISIBLE
            bindingWorker?.rvWorker?.visibility = View.GONE
            bindingWorker?.rlNoInternetFragWorke?.visibility=View.GONE
        } else {
            bindingWorker?.txtNoDataFoundFragWorker?.visibility = View.GONE
            bindingWorker?.rvWorker?.visibility = View.VISIBLE
            bindingWorker?.rlNoInternetFragWorke?.visibility=View.GONE
        }
    }*/

    /*private fun searchApi() {
        viewModel.getImage(this).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        println("data--${it.message}")
                        //hideProgress()
                        resource.data?.let { images ->
                            retrieveImage(images)
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
    }*/

    private fun retrieveImage(response: Response<List<ImageResponse>>) {
        if (response.code() == 403 || response.code() == 401) {
            Toast.makeText(this, "Session Expired ", Toast.LENGTH_SHORT)
            return
        }
        if (response.body() != null) {
            Toast.makeText(this, "Unkown host ", Toast.LENGTH_SHORT)
            imageList = response.body() as MutableList<ImageResponse>


        } else {
            Toast.makeText(this, "Unkown host ", Toast.LENGTH_SHORT)

        }

    }

    private fun setUpToolbar() {
        toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.image_loader_menu_type, menu)
        return true
    }

    @ExperimentalPagingApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuItemGridView -> {
                println("You have selected Grid View")
                imageApi(1)
                true
            }
            R.id.menuItemListView -> {
                println("You have selected List View")
                imageApi(2)

                true
            }
            R.id.menuItemViewPager -> {
                println("You have selected View Pager View")
                imageApi(3)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}