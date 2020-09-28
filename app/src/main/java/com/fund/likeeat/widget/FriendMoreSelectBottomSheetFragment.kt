package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.data.FriendLink
import com.fund.likeeat.data.FriendLinkFavoriteUpdate
import com.fund.likeeat.databinding.BottomSheetFriendMoreBinding
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.INTENT_KEY_FRIEND
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*

class FriendMoreSelectBottomSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetFriendMoreBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val friend: FriendLink? = arguments?.getParcelable(INTENT_KEY_FRIEND)

        friend?.let { friend1 ->
            if (friend1.isFav) {
                binding.textFavorite.text = "즐겨찾기 삭제"
            } else {
                binding.textFavorite.text = "즐겨찾기 추가"
            }

            binding.layoutFavorite.setOnClickListener {
                doUpdateFavorite(friend1)
            }

            binding.layoutDelete.setOnClickListener {
                doDeleteFriend(friend1.id)
            }
        }

        return binding.root
    }

    fun doUpdateFavorite(friend: FriendLink) {

        var bSendUserInfoSuccess = false
        GlobalScope.launch(Dispatchers.Default) {
            try {
                LikeEatRetrofit.getService().updateFriend(friend.id, FriendLinkFavoriteUpdate(!friend.isFav)).apply {
                    if (isSuccessful) {
                        bSendUserInfoSuccess = true
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            while (!bSendUserInfoSuccess) {
                delay(1000)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "즐겨찾기 처리 완료", Toast.LENGTH_LONG).show()
                RetrofitProcedure.getFriends()
                dismiss()
            }
        }
    }

    fun doDeleteFriend(friendId: Long) {

        var bSendUserInfoSuccess = false
        GlobalScope.launch(Dispatchers.Default) {
            try {
                LikeEatRetrofit.getService().deleteFriend(friendId).apply {
                    if (isSuccessful) {
                        bSendUserInfoSuccess = true
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            while (!bSendUserInfoSuccess) {
                delay(1000)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "친구 삭제 완료", Toast.LENGTH_LONG).show()
                RetrofitProcedure.getFriends()
                dismiss()
            }
        }
    }
}