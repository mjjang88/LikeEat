package com.fund.likeeat.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.data.KakaoFriend
import com.fund.likeeat.databinding.ListItemManageFriendBinding
import com.fund.likeeat.utilities.INTENT_KEY_FRIEND
import com.fund.likeeat.viewmodels.FriendViewModel
import com.fund.likeeat.widget.FriendMoreSelectBottomSheetFragment

class FriendManageListAdapter: ListAdapter<KakaoFriend, RecyclerView.ViewHolder>(KakaoFriendDiffCallback()) {

    var mFriendViewModel: FriendViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return KakaoFriendHolder(ListItemManageFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kakaoFriend = getItem(position)
        (holder as KakaoFriendHolder).bind(kakaoFriend)
    }

    inner class KakaoFriendHolder(private val binding: ListItemManageFriendBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KakaoFriend) {
            binding.apply {
                friend = item
                executePendingBindings()

                imageMore.setOnClickListener {
                    val friendMoreSelectBottomSheetFragment = FriendMoreSelectBottomSheetFragment()
                    friendMoreSelectBottomSheetFragment.arguments = Bundle().apply {
                        putParcelable(INTENT_KEY_FRIEND, mFriendViewModel?.getFriendLinkById(item.uid))
                    }
                    friendMoreSelectBottomSheetFragment.show((binding.root.context as FragmentActivity).supportFragmentManager, friendMoreSelectBottomSheetFragment.tag)
                }
            }
        }
    }
}