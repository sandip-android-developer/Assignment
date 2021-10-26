package com.example.viewpaggerdemo.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpaggerdemo.databinding.LoadStateViewBinding

class ProgressLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ProgressLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        holder.binding.loadStateRetry.isVisible = loadState !is LoadState.Loading
        holder.binding.loadStateErrorMessage.isVisible = loadState !is LoadState.Loading
        holder.binding.loadStateProgress.isVisible = loadState is LoadState.Loading

        if (loadState is LoadState.Error) {
            holder.binding.loadStateErrorMessage.text = loadState.error.localizedMessage
        }

        holder.binding.loadStateRetry.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val loadStateViewBinding: LoadStateViewBinding =
            LoadStateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(
            loadStateViewBinding
        )
    }

    class LoadStateViewHolder(mBinding: LoadStateViewBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        val binding = mBinding
    }
}