package com.fund.likeeat.widget

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.BottomSheetReivewMoreBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.ui.AddReviewActivity
import com.fund.likeeat.ui.MainActivity
import com.fund.likeeat.ui.ModifyReviewAcitivity
import com.fund.likeeat.ui.ModifyReviewDetailActivity
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import java.util.ArrayList

class ReviewMoreBottomSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetReivewMoreBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initReview(binding)

        return binding.root
    }

    private fun initReview(binding: BottomSheetReivewMoreBinding) {

        binding.layoutEdit.setOnClickListener {
            val reviews = arguments?.getParcelableArray(INTENT_KEY_REVIEW)

            if (reviews != null) {
                val intent = Intent(requireContext(), ModifyReviewAcitivity::class.java)
                intent.putParcelableArrayListExtra(INTENT_KEY_REVIEW,
                    reviews.toList() as ArrayList<out Parcelable>?
                )
                startActivity(intent)
            } else {
                val review: Review? = arguments?.getParcelable(INTENT_KEY_REVIEW)
                review?.let {
                    val intent = Intent(requireContext(), ModifyReviewDetailActivity::class.java)
                    intent.putExtra(INTENT_KEY_REVIEW, review)
                    startActivity(intent)
                }
            }

            dismiss()
        }

        binding.layoutDelete.setOnClickListener {

            val reviews = arguments?.getParcelableArray(INTENT_KEY_REVIEW)

            if (reviews != null) {
                CustomAlertDialog(requireContext())
                    .setTitle("알림")
                    .setMessage("등록한 맛집을 완전히 삭제합니다.\n계속 하시겠습니까?")
                    .setPositiveButton("삭제") {

                    }.setNegativeButton("취소") {
                    }.show()

            } else {
                val review: Review? = arguments?.getParcelable(INTENT_KEY_REVIEW)
                review?.let {
                    CustomAlertDialog(requireContext())
                        .setTitle("알림")
                        .setMessage("등록한 후기를 완전히 삭제합니다.\n계속 하시겠습니까?")
                        .setPositiveButton("삭제") {
                            doDeleteReview(review.id)
                        }.setNegativeButton("취소") {
                        }.show()
                }
            }
        }
    }

    fun doDeleteReview(reviewId: Long) {

        var bSendUserInfoSuccess = false
        GlobalScope.launch(Dispatchers.Default) {
            try {
                LikeEatRetrofit.getService().deleteReview(reviewId).apply {
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
                Toast.makeText(requireContext(), "리뷰 삭제 완료", Toast.LENGTH_LONG).show()
                RetrofitProcedure.getUserReview(MyApplication.pref.uid)
                dismiss()
            }
        }
    }
}