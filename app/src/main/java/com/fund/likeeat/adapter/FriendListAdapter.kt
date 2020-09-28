package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.databinding.ListItemFriendBinding

class FriendListAdapter: ListAdapter<KakaoFriend, RecyclerView.ViewHolder>(KakaoFriendDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KakaoFriendHolder(ListItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kakaoFriend = getItem(position)
        (holder as KakaoFriendHolder).bind(kakaoFriend)
    }

    inner class KakaoFriendHolder(private val binding: ListItemFriendBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KakaoFriend) {
            binding.apply {
                friend = item
                executePendingBindings()
            }
        }
    }
}