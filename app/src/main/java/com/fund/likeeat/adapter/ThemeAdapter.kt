package com.fund.likeeat.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ItemThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.widget.OnSelectEditListener
import com.fund.likeeat.widget.SetThemeBottomSheet
import kotlinx.android.synthetic.main.item_title.view.*
import kotlinx.coroutines.selects.select

class ThemeAdapter(val fragmentManager: FragmentManager): ListAdapter<Theme, RecyclerView.ViewHolder>(ThemeDiffCallback()) {

    var addThemeListener: OnClickAddThemeListener? = null
    var selectEditListener: OnSelectEditListener? = null
    var clickCardListener : OnClickCardListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_TITLE -> TitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false))
            VIEW_TYPE_CONTENT -> ThemeViewHolder(ItemThemeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> FooterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_action_add_theme, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            VIEW_TYPE_TITLE -> (holder as TitleViewHolder).title.text = MyApplication.applicationContext().resources.getString(R.string.title_my_theme)
            VIEW_TYPE_CONTENT -> {
                val theme = getItem(position)
                (holder as ThemeViewHolder).bind(theme)
            }
            VIEW_TYPE_FOOTER -> {
                addThemeListener?.let { listener ->
                    (holder as FooterViewHolder).itemView.setOnClickListener{ listener.onClick() }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> VIEW_TYPE_TITLE
            itemCount - 1 -> VIEW_TYPE_FOOTER
            else -> VIEW_TYPE_CONTENT
        }
    }

    inner class TitleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.text_title
    }

    inner class FooterViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    inner class ThemeViewHolder(private val binding: ItemThemeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Theme) {
            clickCardListener?.let { listener ->
                binding.cardLayout.setOnClickListener { listener.onClick(item.id, item.name) }
            }

            if(item.name == MyApplication.applicationContext().resources.getString(R.string.theme_all)) {
                binding.imageMore.visibility = View.GONE
            }

            binding.imageMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putLong("THEME_ID", item.id)

                val bottomSheetFragment =
                    SetThemeBottomSheet(object: OnSelectEditListener {
                        override fun onSelectEdit(themeId: Long?) {
                            selectEditListener?.onSelectEdit(themeId)
                        }

                    })
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            }
            binding.apply {
                theme = item
                executePendingBindings()
            }
        }
    }


    fun setOnAddThemeListener(li: OnClickAddThemeListener) {
        addThemeListener = li
    }

    fun setOnClickCardListener(li: OnClickCardListener) {
        clickCardListener = li
    }

    fun setOnSelectEditListener(li: OnSelectEditListener){
        selectEditListener = li
    }

    companion object {
        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_CONTENT = 1
        const val VIEW_TYPE_FOOTER = 2
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

interface OnClickAddThemeListener {
    fun onClick()
}

interface OnClickCardListener {
    fun onClick(themeId: Long, themeName: String = "")
}
