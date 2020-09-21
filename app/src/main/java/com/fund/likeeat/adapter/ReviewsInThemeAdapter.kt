package com.fund.likeeat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ItemOnlyReviewsBinding

class ReviewsInThemeAdapter: ListAdapter<Review, RecyclerView.ViewHolder>(ReviewsInThemeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsInThemeViewHolder(ItemOnlyReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReviewsInThemeViewHolder).bind(getItem(position))
    }

    class ReviewsInThemeViewHolder(private val binding: ItemOnlyReviewsBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(item: Review) {
            binding.cardLayout.setOnClickListener {
                Log.i("CARD_ITEM_ID", item.id.toString())
                Log.i("CARD_ITEM_UID", item.uid.toString())
                Log.i("CARD_ITEM_CATEGORY", item.category.toString())
                Log.i("CARD_ITEM_PLACE_NAME", item.place_name.toString())
                Log.i("CARD_ITEM_ADDRESS_NAME", item.address_name.toString())
            }
            binding.apply {
                review = item
                executePendingBindings()
            }
        }
    }

}

class ReviewsInThemeDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return (oldItem.id) == (newItem.id)
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

}

