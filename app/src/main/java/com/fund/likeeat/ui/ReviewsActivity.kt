package com.fund.likeeat.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsAdapter
import com.fund.likeeat.databinding.ActivityReviewsBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.ReviewsViewModel
import com.kakao.friends.AppFriendContext
import com.kakao.friends.AppFriendOrder
import com.kakao.friends.response.AppFriendsResponse
import com.kakao.kakaotalk.callback.TalkResponseCallback
import com.kakao.kakaotalk.v2.KakaoTalkService
import com.kakao.network.ErrorResult
import com.kakao.sdk.talk.TalkApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ReviewsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReviewsBinding
    private val reviewViewModel: ReviewsViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityReviewsBinding>(
            this,
            R.layout.activity_reviews
        )
        binding.lifecycleOwner = this

        Log.i("UID_ATTACHED", intent.getLongExtra("uid", UID_DETACHED).toString())

        val adapter = ReviewsAdapter()
        binding.recyclerViewReviewList.adapter = adapter

        reviewViewModel.review.observe(this) {
            reviewViewModel.getReviewFullList(it)
        }
        reviewViewModel.reviewFull.observe(this) {
            adapter.submitList(it)
        }

        val appFriendCOntext = AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc")
        KakaoTalkService.getInstance().requestAppFriends(
            appFriendCOntext,
            object : TalkResponseCallback<AppFriendsResponse>() {
                override fun onNotKakaoTalkUser() {
                    Log.e("KAKAO_API", "카카오톡 사용자가 아님")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult)
                }

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("KAKAO_API", "친구 조회 실패: " + errorResult)
                }

                override fun onSuccess(result: AppFriendsResponse?) {
                    Log.i("KAKAO_API", "친구 조회 성공")

                    for (friend in result!!.friends) {
                        Log.d("KAKAO_API", friend.toString())
                        val uuid = friend.uuid // 메시지 전송 시 사용
                    }
                }
            })
    }
}