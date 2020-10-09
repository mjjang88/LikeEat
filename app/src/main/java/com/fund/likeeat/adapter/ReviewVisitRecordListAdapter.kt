package com.fund.likeeat.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ListItemReviewVisitRecordBinding
import com.fund.likeeat.manager.*
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.widget.ReviewMoreBottomSheetFragment

class ReviewVisitRecordListAdapter: ListAdapter<Review, RecyclerView.ViewHolder>(ReviewVisitRecordDiffCallback()) {

    var isFixEnable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewVisitRecordHolder(ListItemReviewVisitRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = getItem(position)
        (holder as ReviewVisitRecordHolder).bind(review)
    }

    inner class ReviewVisitRecordHolder(
        private val binding: ListItemReviewVisitRecordBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.apply {
                review = item
                executePendingBindings()

                if (!item.serviceQuality.isNullOrBlank()) {
                    val drawable = root.resources.getDrawable(getEvaluationSmallImageByName(item.serviceQuality), null)
                    binding.btnEvaluation.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                    binding.btnEvaluation.text = item.serviceQuality
                    binding.btnEvaluation.visibility = View.VISIBLE
                }

                if (!item.companions.isNullOrBlank()) {
                    val drawable = root.resources.getDrawable(getCompanionSmallImageByName(item.companions), null)
                    binding.btnCompanion.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                    binding.btnCompanion.text = item.companions
                    binding.btnCompanion.visibility = View.VISIBLE
                }

                if (!item.priceRange.isNullOrBlank()) {
                    val drawable = root.resources.getDrawable(getPriceSmallImageByName(item.priceRange), null)
                    binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                    binding.btnPrice.text = item.priceRange
                    binding.btnPrice.visibility = View.VISIBLE
                }

                if (!item.toliets.isNullOrBlank()) {
                    val drawable = root.resources.getDrawable(getToiletSmallImageByName(item.toliets), null)
                    binding.btnRestroom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                    binding.btnRestroom.text = item.toliets
                    binding.btnRestroom.visibility = View.VISIBLE
                }

                if (!item.revisit.isNullOrBlank()) {
                    val drawable = root.resources.getDrawable(getRevisitSmallImageByName(item.revisit), null)
                    binding.btnReVisit.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                    binding.btnReVisit.text = item.revisit
                    binding.btnReVisit.visibility = View.VISIBLE
                }

                binding.imageMore.setOnClickListener {
                    val reviewMoreBottomSheetFragment = ReviewMoreBottomSheetFragment()
                    reviewMoreBottomSheetFragment.arguments = Bundle().apply { putParcelable(
                        INTENT_KEY_REVIEW, item) }
                    reviewMoreBottomSheetFragment.show((binding.root.context as FragmentActivity).supportFragmentManager, reviewMoreBottomSheetFragment.tag)
                }

                if (!isFixEnable) {
                    binding.imageMore.visibility = View.GONE
                }
            }
        }
    }
}

class ReviewVisitRecordDiffCallback: DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}