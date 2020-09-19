package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.databinding.LayoutImageTextBinding

class AddReviewBottomSheetListAdapter : ListAdapter<GridItem, RecyclerView.ViewHolder>(GridItemDiffCallback())  {

    var selectedPosition : Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GridItemViewHolder(
            LayoutImageTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val gridItem = getItem(position)
        holder.itemView.isSelected = selectedPosition == position
        (holder as GridItemViewHolder).bind(gridItem)
    }

    inner class GridItemViewHolder(
        private val binding: LayoutImageTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GridItem) {
            binding.apply {
                gridItem = item
                executePendingBindings()
                if (itemView.isSelected) {
                    image.imageAlpha = 255
                    text.alpha = 1f
                } else {
                    image.imageAlpha = 50
                    text.alpha = 0.3f
                }
            }

            binding.setClickListener {
                val prevSelectedPosition = selectedPosition
                selectedPosition = layoutPosition
                notifyItemChanged(prevSelectedPosition)
                notifyItemChanged(selectedPosition)
            }
        }
    }

    fun getSelectedItem(): GridItem {
        if (selectedPosition == RecyclerView.NO_POSITION) {
            return GridItem("", 0)
        }
        return getItem(selectedPosition)
    }

    fun setSelectItem(name: String) {
        for (index in 0 until itemCount) {
            if (getItem(index).name == name) {
                selectedPosition = index
                notifyItemChanged(index)
            }
        }
    }
}

private class GridItemDiffCallback : DiffUtil.ItemCallback<GridItem>() {

    override fun areItemsTheSame(oldItem: GridItem, newItem: GridItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GridItem, newItem: GridItem): Boolean {
        return oldItem == newItem
    }
}

data class GridItem (
    val name: String,
    val imageId: Int
)