package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ItemOnlyReviewsBinding

class ReviewsInThemeAdapter: ListAdapter<Review, RecyclerView.ViewHolder>(ReviewsInThemeDiffCallback()) {
    var cardLongClickListener: CardLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsInThemeViewHolder(ItemOnlyReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReviewsInThemeViewHolder).bind(getItem(position))
    }

    inner class ReviewsInThemeViewHolder(private val binding: ItemOnlyReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            cardLongClickListener?.let { listener ->
                binding.cardLayout.setOnLongClickListener{
                    listener.onLongClick(item.id)
                    true
                }
            }

            binding.apply {
                review = item
                executePendingBindings()
            }
        }
    }

    fun setOnCardLongClickListener(li: CardLongClickListener) {
        cardLongClickListener = li
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

interface CardLongClickListener {
    fun onLongClick(reviewId: Long)
}
