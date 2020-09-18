package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.databinding.LayoutColorTagWithTextBinding

class ColorListAdapter(val li: ColorSelectedListener) : ListAdapter<ColorItem, RecyclerView.ViewHolder>(ColorItemDiffCallback())  {
    var selectedPosition : Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorItemViewHolder(
            LayoutColorTagWithTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val colorItem = getItem(position)
        holder.itemView.isSelected = selectedPosition == position
        (holder as ColorItemViewHolder).bind(colorItem)
    }

    inner class ColorItemViewHolder(
        private val binding: LayoutColorTagWithTextBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ColorItem) {
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
                li.onColorSelected(item.colorCode)
            }
        }
    }

    fun submitList(list: MutableList<ColorItem>?, selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        super.submitList(list)
    }
}

private class ColorItemDiffCallback : DiffUtil.ItemCallback<ColorItem>() {
    override fun areItemsTheSame(oldItem: ColorItem, newItem: ColorItem): Boolean {
        return oldItem.colorCode == newItem.colorCode
    }

    override fun areContentsTheSame(oldItem: ColorItem, newItem: ColorItem): Boolean {
        return oldItem == newItem
    }

}

data class ColorItem (
    val colorCode: Int,
    val name: String
)

interface ColorSelectedListener {
    fun onColorSelected(colorCode: Int)
}