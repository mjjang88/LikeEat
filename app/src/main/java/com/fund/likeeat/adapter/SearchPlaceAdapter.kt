package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ItemOnlyReviewsBinding

class SearchPlaceAdapter: ListAdapter<Review, RecyclerView.ViewHolder>(PlacesInThemeDiffCallback()) {
    var cardClickListener: CardClickListener? = null
    val selectedSet = hashSetOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsInThemeViewHolder(
            ItemOnlyReviewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item in selectedSet) {
            (holder as ReviewsInThemeViewHolder).itemView.setBackgroundResource(R.color.black06)
        } else {
            (holder as ReviewsInThemeViewHolder).itemView.setBackgroundResource(R.color.white)
        }
        (holder as ReviewsInThemeViewHolder).bind(getItem(position))
    }

    inner class ReviewsInThemeViewHolder(private val binding: ItemOnlyReviewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            binding.apply {
                review = item
                executePendingBindings()
            }

            binding.setClickListener {
                if (item in selectedSet) {
                    selectedSet.remove(item)
                } else selectedSet.add(item)
                notifyItemChanged(layoutPosition)
                cardClickListener?.let { listener ->
                    listener.onClick(item)
                }
            }
        }

    }

    fun setOnCardClickListener(li: CardClickListener) {
        cardClickListener = li
    }
}

interface CardClickListener {
    fun onClick(review: Review)
}
