package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.ReviewFull
import com.fund.likeeat.databinding.ItemReviewsBinding
import com.google.android.material.chip.Chip

class ReviewsAdapter: ListAdapter<ReviewFull, RecyclerView.ViewHolder>(ReviewDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder(ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reviewFull = getItem(position)
        (holder as ReviewsViewHolder).bind(reviewFull)
    }

    class ReviewsViewHolder(private val binding: ItemReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewFull) {
            binding.apply {
                reviewFull = item

                item.theme.forEach {theme ->
                    val chip = Chip(root.context).apply {
                        text = theme.name
                    }
                    chipGroupTag.addView(chip)
                }

                setClickListener {
                    /*val context = binding.root.context
                    val intent = Intent(context, ViewReviewActivity::class.java)
                    intent.putExtra(INTENT_KEY_REVIEW, item)
                    context.startActivity(intent)*/
                }

                executePendingBindings()
            }
        }
    }

}

private class ReviewDiffCallback: DiffUtil.ItemCallback<ReviewFull>() {
    override fun areItemsTheSame(oldItem: ReviewFull, newItem: ReviewFull): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ReviewFull, newItem: ReviewFull): Boolean {
        return oldItem == newItem
    }

}