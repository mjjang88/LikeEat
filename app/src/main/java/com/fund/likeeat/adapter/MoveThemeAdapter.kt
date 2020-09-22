package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ListItemAddReviewThemeBinding

class MoveThemeAdapter(): ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {
    var selectedPosition: Int = RecyclerView.NO_POSITION

    var addThemeListener: OnClickAddThemeListener? = null
    var clickCardListener : OnClickCardListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(ListItemAddReviewThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        holder.itemView.isSelected = selectedPosition == position
        (holder as ThemeViewHolder).bind(theme)
    }

    inner class ThemeViewHolder(private val binding: ListItemAddReviewThemeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Theme) {
            binding.apply {
                theme = item
                executePendingBindings()

                if(itemView.isSelected) {
                    layoutTheme.setBackgroundResource(R.color.black06)
                } else {
                    layoutTheme.setBackgroundResource(R.color.white)
                }
            }

            binding.setClickListener {
                val prevSelectedPosition = selectedPosition
                selectedPosition = layoutPosition
                notifyItemChanged(prevSelectedPosition)
                notifyItemChanged(selectedPosition)

                clickCardListener?.onClick(item.id)
            }
        }
    }

    fun setOnAddThemeListener(li: OnClickAddThemeListener) {
        addThemeListener = li
    }

    fun setOnClickCardListener(li: OnClickCardListener) {
        clickCardListener = li
    }
}
