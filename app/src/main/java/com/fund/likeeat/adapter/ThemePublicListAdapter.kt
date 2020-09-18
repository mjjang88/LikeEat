package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.databinding.LayoutColorTagWithTextBinding
import com.fund.likeeat.databinding.LayoutImageTextBinding
import com.fund.likeeat.databinding.LayoutPublicImageTextBinding

class ThemePublicListAdapter(val li: PublicSelectedListener) : ListAdapter<GridPublicItem, RecyclerView.ViewHolder>(GridPublicItemDiffCallback())  {
    var selectedPosition : Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PublicItemViewHolder(
            LayoutPublicImageTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val publicItem = getItem(position)
        holder.itemView.isSelected = selectedPosition == position
        (holder as PublicItemViewHolder).bind(publicItem)
    }

    inner class PublicItemViewHolder(
        private val binding: LayoutPublicImageTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GridPublicItem) {
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
                li.onPublicSelected(item.isPublic)
            }
        }
    }

    fun submitList(list: MutableList<GridPublicItem>?, selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        super.submitList(list)
    }
}

private class GridPublicItemDiffCallback : DiffUtil.ItemCallback<GridPublicItem>() {
    override fun areItemsTheSame(oldItem: GridPublicItem, newItem: GridPublicItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GridPublicItem, newItem: GridPublicItem): Boolean {
        return oldItem == newItem
    }

}

interface PublicSelectedListener {
    fun onPublicSelected(isPublic: Boolean)
}

data class GridPublicItem (
    val name: String,
    val imageId: Int,
    val isPublic: Boolean
)