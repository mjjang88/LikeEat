package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.R
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ActivityAddReviewBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.ReviewNWWrite
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddReviewActivity : AppCompatActivity()  {

    private val addReviewViewModel: AddReviewViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    lateinit var mPlace: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAddReviewBinding>(
            this,
            R.layout.activity_add_review
        )

        initPlace(binding)

        initComponent(binding)
    }

    private fun initPlace(binding: ActivityAddReviewBinding) {
        val place = intent.getParcelableExtra<Place>(INTENT_KEY_PLACE)

        binding.place = place
        mPlace = place!!
    }

    private fun initComponent(binding: ActivityAddReviewBinding) {

        lifecycleScope.launch(Dispatchers.IO) {
            val spinnerAdapter = ArrayAdapter<String>(this@AddReviewActivity, android.R.layout.simple_spinner_dropdown_item, addReviewViewModel.getThemeList().map { theme -> theme.name })

            withContext(Dispatchers.Main) {
                binding.spinnerTheme.adapter = spinnerAdapter
            }
        }

        binding.btnExtend.setOnClickListener {
            it.visibility = View.GONE
            binding.layoutDetailPage.visibility = View.VISIBLE
        }

        binding.btnReduce.setOnClickListener {
            binding.layoutDetailPage.visibility = View.GONE
            binding.btnExtend.visibility = View.VISIBLE
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnCheck.setOnClickListener {
            doAddingReview()
        }
    }

    private fun doAddingReview() {

        val review = makeReview()

        var bSendUserInfoSuccess = false
        GlobalScope.launch(Dispatchers.Default) {
            try {
                LikeEatRetrofit.getService().addReview(review).apply {
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
                Toast.makeText(this@AddReviewActivity, "리뷰 추가 완료", Toast.LENGTH_LONG).show()
                val intent = Intent(this@AddReviewActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        }


    }

    private fun makeReview(): ReviewNWWrite {

        val uid = MyApplication.pref.uid
        val isPublic = !check_is_public.isChecked
        val category = "맛집"
        val comment = edit_comment.text.toString()
        val visitedDayYmd = "20200808"
        val companions = edit_companion.text.toString()
        val toliets = edit_restroom.text.toString()
        val priceRange = edit_price.text.toString()
        val serviceQuality = edit_evaluation.text.toString()
        val revisit = edit_revisit.text.toString()
        val themeIds = "1,2"

        return ReviewNWWrite(
            -1,
            uid,
            isPublic,
            category,
            comment,
            visitedDayYmd,
            companions,
            toliets,
            priceRange,
            serviceQuality,
            revisit,
            themeIds,
            mPlace,
            null
        )
    }
}