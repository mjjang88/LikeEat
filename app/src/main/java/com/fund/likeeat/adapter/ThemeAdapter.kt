package com.fund.likeeat.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ItemThemeBinding
import com.fund.likeeat.ui.SetThemeBottomSheet

class ThemeAdapter(val fragmentManager: FragmentManager): ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        (holder as ThemeViewHolder).bind(theme)
    }

    inner class ThemeViewHolder(private val binding: ItemThemeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Theme) {
            binding.imageMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("THEME_ID", item.id) // TODO

                val bottomSheetFragment = SetThemeBottomSheet()
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            }
            binding.apply {
                theme = item
                executePendingBindings()
            }
        }
    }
}

private class ThemeDiffCallback: DiffUtil.ItemCallback<Theme>() {
    override fun areItemsTheSame(oldItem: Theme, newItem: Theme): Boolean {
        return (oldItem.uid.toString() + oldItem.name) == (newItem.uid.toString() + newItem.name)
    }

    override fun areContentsTheSame(oldItem: Theme, newItem: Theme): Boolean {
        return oldItem == newItem
    }

}