package com.example.viewpaggerdemo.view.ui.assigment2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpaggerdemo.R
import com.example.viewpaggerdemo.databinding.ItemListLayoutBinding
import com.example.viewpaggerdemo.databinding.ItemViewPagerBinding
import com.example.viewpaggerdemo.model.responsepojo.ImageResponse


class ViewPagerAdapter(private val context: Context, private val type: Int) :
    PagingDataAdapter<ImageResponse, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (type == 3) {
            val bindingViewpager: ItemViewPagerBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_view_pager,
                parent,
                false
            )
            ImageViewHolderWithPager(bindingViewpager)
        } else {
            val bindingList: ItemListLayoutBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.item_list_layout,
                    parent,
                    false
                )
            ImageViewHolderWithList(bindingList)
        }

    }

    inner class ImageViewHolderWithPager(private val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: ImageResponse?) {
            binding.image = response
            binding.executePendingBindings()
        }

    }

    inner class ImageViewHolderWithList(private val binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(response: ImageResponse?) {
            binding.image = response
            binding.executePendingBindings()
        }

        var image: ImageView = itemView.findViewById(R.id.imgViewPager)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ImageResponse>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: ImageResponse,
                newConcert: ImageResponse
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: ImageResponse,
                newConcert: ImageResponse
            ) = oldConcert.equals(newConcert)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image: ImageResponse? = getItem(position)
        if (type == 3) {
            val holderImage = holder as ImageViewHolderWithPager
            holderImage.bind(image)
        } else {
            val holderImage = holder as ImageViewHolderWithList
            holderImage.bind(image)
        }
    }
}