package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ItemReviewsBinding

class ReviewsAdapter: ListAdapter<Review, RecyclerView.ViewHolder>(ReviewDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder(ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = getItem(position)
        (holder as ReviewsViewHolder).bind(review)
    }

    class ReviewsViewHolder(private val binding: ItemReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.apply {
                review = item
                executePendingBindings()
            }
        }
    }

}

private class ReviewDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}