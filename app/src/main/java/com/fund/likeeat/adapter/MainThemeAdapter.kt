package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ItemThemeBinding

class MainThemeAdapter: ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {
    var selectedPosition : Int = 0
    var selectedThemeId: Long = 0

    var selectNavCardListener : OnSelectNavCardListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        holder.itemView.isSelected = selectedPosition == position
        (holder as MainThemeViewHolder).bind(theme)
    }

    inner class MainThemeViewHolder(private val binding: ItemThemeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Theme) {
            binding.themePublic.visibility = View.GONE
            binding.imageMore.visibility = View.GONE

            binding.apply {
                theme = item
                executePendingBindings()

                if (itemView.isSelected) {
                    selectNavCardListener?.onSelectNavCard(item)
                    binding.cardLayout.setBackgroundResource(R.color.black06)
                } else {
                    binding.cardLayout.setBackgroundResource(R.color.white)
                }
            }

            binding.cardLayout.setOnClickListener {
                val prevSelectedPosition = selectedPosition
                selectedPosition = layoutPosition
                notifyItemChanged(prevSelectedPosition)
                notifyItemChanged(selectedPosition)
            }
        }

    }
    fun setOnClickNavCardListener(li: OnSelectNavCardListener) {
        selectNavCardListener = li
    }
}

interface OnSelectNavCardListener {
    fun onSelectNavCard(theme: Theme)
}