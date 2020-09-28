package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.FriendManageListAdapter
import com.fund.likeeat.databinding.ActivityFriendsBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.FriendViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FriendsActivity : AppCompatActivity() {

    val friendViewModel: FriendViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityFriendsBinding>(
            this,
            R.layout.activity_friends
        )

        val favoriteAdapter = FriendManageListAdapter()
        favoriteAdapter.mFriendViewModel = friendViewModel
        binding.listFavorite.adapter = favoriteAdapter
        friendViewModel.favoriteFriends.observe(this) {
            favoriteAdapter.submitList(it)
        }

        val friendAdapter = FriendManageListAdapter()
        friendAdapter.mFriendViewModel = friendViewModel
        binding.listFriends.adapter = friendAdapter
        friendViewModel.friends.observe(this) {
            friendAdapter.submitList(it)
        }

        var isGetFriend = false
        var isGetFriendLink = false
        friendViewModel.kakaofriends.observe(this) {
            isGetFriend = true
            if (isGetFriend && isGetFriendLink) {
                friendViewModel.getFriendList()
            }
        }
        friendViewModel.friendLink.observe(this) {
            isGetFriendLink = true
            if (isGetFriend && isGetFriendLink) {
                friendViewModel.getFriendList()
            }
        }

        binding.btnAddFriend.setOnClickListener {
            val intent = Intent(this, AddFriendActivity::class.java)
            startActivity(intent)
        }
    }
}