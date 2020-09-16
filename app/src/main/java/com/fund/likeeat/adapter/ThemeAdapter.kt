package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ItemThemeBinding


class ThemeAdapter: ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        (holder as ThemeViewHolder).bind(theme)
    }

    class ThemeViewHolder(private val binding: ItemThemeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Theme) {
            binding.apply {
                theme = item
                executePendingBindings()
            }
        }
    }
}

class ThemeDiffCallback: DiffUtil.ItemCallback<Theme>() {
    override fun areItemsTheSame(oldItem: Theme, newItem: Theme): Boolean {
        return (oldItem.uid.toString() + oldItem.name) == (newItem.uid.toString() + newItem.name)
    }

    override fun areContentsTheSame(oldItem: Theme, newItem: Theme): Boolean {
        return oldItem == newItem
    }

}