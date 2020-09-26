package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.databinding.ListItemAddFriendBinding
import com.fund.likeeat.manager.MyApplication

class AddFriendListAdapter: ListAdapter<KakaoFriend, RecyclerView.ViewHolder>(KakaoFriendDiffCallback()) {

    var checkedList: ArrayList<KakaoFriend> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KakaoFriendHolder(ListItemAddFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kakaoFriend = getItem(position)
        (holder as KakaoFriendHolder).bind(kakaoFriend)
    }

    inner class KakaoFriendHolder(private val binding: ListItemAddFriendBinding): RecyclerView.ViewHolder(binding.root), Checkable{
        var check: Boolean = false

        fun bind(item: KakaoFriend) {
            binding.apply {
                friend = item
                executePendingBindings()
            }

            binding.layoutRoot.setOnClickListener {
                toggle()
                updateCheckList(item)
            }
        }

        override fun setChecked(checked: Boolean) {
            check = checked
            updateBackground()
        }

        override fun isChecked(): Boolean {
            return check
        }

        override fun toggle() {
            check = !check
            updateBackground()
        }

        private fun updateCheckList(item: KakaoFriend) {
            if (check) {
                checkedList.add(item)
            } else {
                checkedList.remove(item)
            }
        }

        private fun updateBackground() {
            if (isChecked) {
                binding.layoutRoot.setBackgroundColor(MyApplication.applicationContext().getColor(R.color.colorSelectBackground))
            } else {
                binding.layoutRoot.setBackgroundColor(MyApplication.applicationContext().getColor(R.color.colorSurface))
            }
        }
    }
}

class KakaoFriendDiffCallback: DiffUtil.ItemCallback<KakaoFriend>() {

    override fun areItemsTheSame(oldItem: KakaoFriend, newItem: KakaoFriend): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: KakaoFriend, newItem: KakaoFriend): Boolean {
        return oldItem == newItem
    }
}