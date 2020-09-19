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
    }
}