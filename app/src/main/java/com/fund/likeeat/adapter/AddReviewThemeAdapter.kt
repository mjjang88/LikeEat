package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ListItemAddReviewThemeBinding
import com.fund.likeeat.manager.MyApplication

class AddReviewThemeAdapter: ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {

    var checkedList: ArrayList<Theme> = ArrayList()

    var checkable: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ThemeViewHolder(ListItemAddReviewThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val theme = getItem(position)
        (holder as ThemeViewHolder).bind(theme)
    }

    inner class ThemeViewHolder(private val binding: ListItemAddReviewThemeBinding): RecyclerView.ViewHolder(binding.root), Checkable {
        var check: Boolean = false

        fun bind(item: Theme) {
            binding.apply {
                theme = item
                executePendingBindings()

                checkedList.any {
                    it.id == item.id
                }.also {
                    isChecked = it
                }

                binding.layoutTheme.setOnClickListener {
                    toggle()
                    updateCheckList(item)
                }
            }
        }

        override fun setChecked(checked: Boolean) {
            if (checkable) {
                check = checked
                updateBackground()
            }
        }

        override fun isChecked(): Boolean {
            return check
        }

        override fun toggle() {
            if (checkable) {
                check = !check
                updateBackground()
            }
        }

        private fun updateCheckList(item: Theme) {
            if (checkable) {
                if (check) {
                    checkedList.add(item)
                } else {
                    checkedList.remove(item)
                }
            }
        }

        private fun updateBackground() {
            if (isChecked) {
                binding.layoutTheme.setBackgroundColor(MyApplication.applicationContext().getColor(R.color.colorSelectBackground))
            } else {
                binding.layoutTheme.setBackgroundColor(MyApplication.applicationContext().getColor(R.color.colorSurface))
            }
        }
    }
}