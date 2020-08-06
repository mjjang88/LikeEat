package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Place
import com.fund.likeeat.databinding.ItemReviewsBinding

class ReviewsAdapter: ListAdapter<Place, RecyclerView.ViewHolder>(PlaceDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsViewHolder(ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = getItem(position)
        (holder as ReviewsViewHolder).bind(place)
    }

    class ReviewsViewHolder(private val binding: ItemReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.apply {
                place = item
                
                // 단순히 뷰에 데이터가 뿌려지는지 확인하기 위한 코드. 나중에 다 지워야됨
                // uidText.text = item.uid.toString()
                latText.text = item.x.toString()
                lonText.text = item.y.toString()
                
                executePendingBindings()
            }
        }
    }

}

private class PlaceDiffCallback: DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }

}