package com.fund.likeeat.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsAdapter
import com.fund.likeeat.data.AppDatabase
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceRepository
import com.fund.likeeat.databinding.ActivityReviewsBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.InjectorUtils
import com.fund.likeeat.utilities.SharedPreference
import com.fund.likeeat.utilities.UID_DETACHED
import com.fund.likeeat.viewmodels.ReviewsViewModel
import com.fund.likeeat.viewmodels.ReviewsViewModelFactory

class ReviewsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReviewsBinding
    private val viewModel: ReviewsViewModel by viewModels {
        InjectorUtils.provideReviewsViewModelFactory(this, intent.getLongExtra("uid", UID_DETACHED))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityReviewsBinding>(this, R.layout.activity_reviews)
        binding.lifecycleOwner = this

        Log.i("UID_ATTACHED", intent.getLongExtra("uid", UID_DETACHED).toString())

        val adapter = ReviewsAdapter()
        binding.reviewList.adapter = adapter
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: ReviewsAdapter) {
        viewModel.review?.observe(this) { result ->
            adapter.submitList(result)
        }
    }
}