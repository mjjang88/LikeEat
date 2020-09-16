package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.PlaceServer
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.network.ReviewServerWrite
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.widget.Category
import com.fund.likeeat.widget.CategorySelectBottomSheetFragment
import com.fund.likeeat.widget.SwitchButton
import com.fund.likeeat.widget.ThemeSelectBottomSheetFragment
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.android.synthetic.main.layout_image_text.view.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddReviewActivity : AppCompatActivity()  {

    private val addReviewViewModel: AddReviewViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    lateinit var mPlace: Place

    var themeList: List<Theme>? = null

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
            val switchBtn = it as SwitchButton
            switchBtn.toggle()
            if (switchBtn.check) {
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

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnOk.setOnClickListener {
            //doAddingReview()
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

        if (!review.themeIds.isNullOrBlank()) {
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

    }

    fun getCategoryImageByName(name: String): Int {
        return when (name) {
            "한식" -> Category.KoreanFood.imageId
            "중식" -> Category.ChineseFood.imageId
            "일식" -> Category.JapaneseFood.imageId
            "양식" -> Category.WesternFood.imageId
            "아시안" -> Category.AsianFood.imageId
            "세계" -> Category.WorldFood.imageId
            "분식" -> Category.SnackBar.imageId
            "카페" -> Category.Cafe.imageId
            "패스트푸드" -> Category.FastFood.imageId
            else -> -1
        }
    }

    /*private fun doAddingReview() {

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
    }*/

    /*private fun makeReview(): ReviewServerWrite {

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
        val themeIds = themeList?.get(spinner_theme.selectedItemPosition)?.id.toString()

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
    }*/
}