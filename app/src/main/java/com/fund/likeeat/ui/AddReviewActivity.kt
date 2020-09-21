package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewThemeAdapter
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityAddReviewBinding
import com.fund.likeeat.manager.*
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.PlaceServer
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.network.ReviewServerWrite
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.widget.*
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

        addReviewViewModel.editedReview.observe(this) {
            it?.let { review -> updateReview(binding, review) }
        }
    }

    private fun initPlace(binding: ActivityAddReviewBinding) {
        val place = intent.getParcelableExtra<Place>(INTENT_KEY_PLACE)

        binding.place = place
        mPlace = place!!
    }

    private fun initComponent(binding: ActivityAddReviewBinding) {

        binding.btnCategory.setOnClickListener {
            val categoryBottomSheetFragment = CategorySelectBottomSheetFragment()
            categoryBottomSheetFragment.addReviewViewModel = addReviewViewModel
            categoryBottomSheetFragment.show(supportFragmentManager, categoryBottomSheetFragment.tag)
        }

        binding.btnExtend.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                binding.layoutDetailPage.visibility = View.VISIBLE
            } else {
                binding.layoutDetailPage.visibility = View.GONE
            }
        }

        binding.checkIsPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.share)
            } else {
                buttonView.text = getString(R.string.not_share)
            }
        }

        binding.layoutAddTheme.setOnClickListener {
            val themeBottomSheetFragment = ThemeSelectBottomSheetFragment()
            themeBottomSheetFragment.addReviewViewModel = addReviewViewModel
            themeBottomSheetFragment.show(supportFragmentManager, themeBottomSheetFragment.tag)
        }

        binding.btnVisitDate.setOnClickListener {
            val visitDateBottomSheetFragment = VisitDateSelectBottomSheetFragment()
            visitDateBottomSheetFragment.addReviewViewModel = addReviewViewModel
            visitDateBottomSheetFragment.show(supportFragmentManager, visitDateBottomSheetFragment.tag)
        }

        binding.btnEvaluation.setOnClickListener {
            val evalBottomSheetFragment = EvaluationSelectBottomSheetFragment()
            evalBottomSheetFragment.addReviewViewModel = addReviewViewModel
            evalBottomSheetFragment.show(supportFragmentManager, evalBottomSheetFragment.tag)
        }

        binding.btnCompanion.setOnClickListener {
            val companionBottomSheetFragment = CompanionSelectBottomSheetFragment()
            companionBottomSheetFragment.addReviewViewModel = addReviewViewModel
            companionBottomSheetFragment.show(supportFragmentManager, companionBottomSheetFragment.tag)
        }

        binding.btnPrice.setOnClickListener {
            val priceBottomSheetFragment = PriceSelectBottomSheetFragment()
            priceBottomSheetFragment.addReviewViewModel = addReviewViewModel
            priceBottomSheetFragment.show(supportFragmentManager, priceBottomSheetFragment.tag)
        }

        binding.btnRestroom.setOnClickListener {
            val toiletBottomSheetFragment = ToiletSelectBottomSheetFragment()
            toiletBottomSheetFragment.addReviewViewModel = addReviewViewModel
            toiletBottomSheetFragment.show(supportFragmentManager, toiletBottomSheetFragment.tag)
        }

        binding.btnReVisit.setOnClickListener {
            val revisitBottomSheetFragment = RevisitSelectBottomSheetFragment()
            revisitBottomSheetFragment.addReviewViewModel = addReviewViewModel
            revisitBottomSheetFragment.show(supportFragmentManager, revisitBottomSheetFragment.tag)
        }

        binding.editComment.setOnEditorActionListener { v, actionId, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (v.text.toString().isNotBlank()) {
                        v.background = getDrawable(R.drawable.edit_background_shape)
                    } else {
                        v.background = getDrawable(R.drawable.edit_background_red_shape)
                    }
                }
            }

            false
        }

        binding.editComment.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.length?.let {
                    if (it > 0) {
                        binding.editComment.background = getDrawable(R.drawable.edit_background_shape)
                    } else {
                        binding.editComment.background = getDrawable(R.drawable.edit_background_red_shape)
                    }
                }

                setEnableBtnOk()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnOk.setOnClickListener {
            doAddingReview()
        }
    }

    private fun updateReview(binding: ActivityAddReviewBinding, review: ReviewServerWrite) {

        if (review.category.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_red, null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = getString(R.string.category)
            binding.btnCategory.setTextColor(getColor(R.color.colorPrimary))
        } else {
            val drawable = resources.getDrawable(getCategoryImageByName(review.category!!), null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = review.category
            binding.btnCategory.setTextColor(getColor(R.color.colorBlack))
        }

        if (review.themeIds != null) {
            val adapter = AddReviewThemeAdapter()
            binding.listTheme.adapter = adapter
            adapter.checkable = false

            lifecycleScope.launch(Dispatchers.IO) {
                val themeList = addReviewViewModel.getThemeList()
                val themeIds = addReviewViewModel.editedReview.value?.themeIds?.split(",")
                val checkedList: ArrayList<Theme> = ArrayList()

                themeIds?.forEach { themeId ->
                    if (themeId.isNullOrBlank()) return@forEach
                    themeList.find {
                        it.id == themeId.toLong()
                    }.apply {
                        if (this != null) {
                            checkedList.add(this)
                        }
                    }
                }

                adapter.submitList(checkedList)
            }
        }

        if (review.visitedDayYmd.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnVisitDate.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnVisitDate.text = getString(R.string.visit_date)
        } else {
            val drawable = resources.getDrawable(R.drawable.ic_select_date, null)
            binding.btnVisitDate.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnVisitDate.text = calcVisitDate(review.visitedDayYmd?: "")
        }

        if (review.serviceQuality.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnEvaluation.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnEvaluation.text = getString(R.string.evaluation)
        } else {
            val drawable = resources.getDrawable(getEvaluationImageByName(review.serviceQuality!!), null)
            binding.btnEvaluation.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnEvaluation.text = review.serviceQuality
        }

        if (review.companions.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnCompanion.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCompanion.text = getString(R.string.companion)
        } else {
            val drawable = resources.getDrawable(getCompanionImageByName(review.companions!!), null)
            binding.btnCompanion.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCompanion.text = review.companions
        }

        if (review.priceRange.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnPrice.text = getString(R.string.price)
        } else {
            val drawable = resources.getDrawable(getPriceImageByName(review.priceRange!!), null)
            binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnPrice.text = review.priceRange
        }

        if (review.toliets.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnRestroom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnRestroom.text = getString(R.string.restroom)
        } else {
            val drawable = resources.getDrawable(getToiletImageByName(review.toliets!!), null)
            binding.btnRestroom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnRestroom.text = review.toliets
        }

        if (review.revisit.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnReVisit.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnReVisit.text = getString(R.string.re_visit)
        } else {
            val drawable = resources.getDrawable(getRevisitImageByName(review.revisit!!), null)
            binding.btnReVisit.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnReVisit.text = review.revisit
        }

        setEnableBtnOk()
    }

    fun calcVisitDate(date: String): String {
        if (date.length == 8) {
            val year = date.substring(0, 4)
            val month = date.substring(4, 6)
            val day = date.substring(6, 8)

            return "$month.$day"
        }
        return date
    }

    fun setEnableBtnOk() {
        btn_ok.isEnabled = edit_comment.text.length > 0 && !addReviewViewModel.editedReview.value?.category.isNullOrBlank()
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
                RetrofitProcedure.getUserReview(MyApplication.pref.uid)
            }
        }
    }

    private fun makeReview(): ReviewServerWrite {

        val uid = MyApplication.pref.uid
        val isPublic = check_is_public.isChecked
        val category = addReviewViewModel.editedReview.value?.category?: ""
        val comment = edit_comment.text.toString()
        val visitedDayYmd = addReviewViewModel.editedReview.value?.visitedDayYmd
        val companions = addReviewViewModel.editedReview.value?.companions?: ""
        val toliets = addReviewViewModel.editedReview.value?.toliets?: ""
        val priceRange = addReviewViewModel.editedReview.value?.priceRange?: ""
        val serviceQuality = addReviewViewModel.editedReview.value?.serviceQuality?: ""
        val revisit = addReviewViewModel.editedReview.value?.revisit?: ""
        val themeIds = addReviewViewModel.editedReview.value?.themeIds?: ""

        return ReviewServerWrite(
            isPublic,
            category,
            comment,
            visitedDayYmd,
            companions,
            toliets,
            priceRange,
            serviceQuality,
            themeIds,
            uid,
            revisit,
            PlaceServer(mPlace)
        )
    }
}