package com.fund.likeeat.adapter

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.ReviewFull
import com.fund.likeeat.databinding.ItemReviewsBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.manager.getCategorySmallImageByName
import com.fund.likeeat.ui.MapActivity
import com.fund.likeeat.ui.ReviewDetailActivity
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW

class ReviewsAdapter: ListAdapter<ReviewFull, RecyclerView.ViewHolder>(ReviewDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder(
            ItemReviewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reviewFull = getItem(position)
        (holder as ReviewsViewHolder).bind(reviewFull)
    }

    class ReviewsViewHolder(private val binding: ItemReviewsBinding): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(item: ReviewFull) {
            binding.apply {
                reviewFull = item
                executePendingBindings()

                item.review.category?.let {
                    reviewConceptImage.setImageResource(getCategorySmallImageByName(it))
                }

                layoutTag.removeAllViews()
                item.theme.filter {
                    it.name != MyApplication.applicationContext().getString(R.string.theme_all)
                }.forEach { theme ->
                    val textView = TextView(root.context).apply {
                        text = "#${theme.name}"
                        setTypeface(null, Typeface.BOLD)
                    }
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        rightMargin = MyApplication.applicationContext().resources.getDimensionPixelSize(R.dimen.margin_normal)
                    }
                    layoutTag.addView(textView, param)
                }

                if (layoutTag.childCount == 0) {
                    layoutTag.visibility = View.GONE
                } else {
                    layoutTag.visibility = View.VISIBLE
                }

                setClickListener {
                    val intent = Intent(root.context, ReviewDetailActivity::class.java)
                    intent.putExtra(INTENT_KEY_REVIEW, item.review)
                    root.context.startActivity(intent)
                }

                btnGoMap.setOnClickListener {
                    val intent = Intent(root.context, MapActivity::class.java)
                    intent.putExtra(INTENT_KEY_REVIEW, item.review)
                    root.context.startActivity(intent)
                }
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