package com.fund.likeeat.widget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.MoveThemeAdapter
import com.fund.likeeat.adapter.OnClickCardListener
import com.fund.likeeat.data.PlaceWhenChangeReview
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.BottomSheetSelectNewThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.*
import com.fund.likeeat.viewmodels.AllThemesViewModel
import com.fund.likeeat.viewmodels.ReviewInThemeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoveReviewInThemeBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }
    private val reviewInThemeViewModel : ReviewInThemeViewModel by inject()

    var newThemeId: Long? = null
    var themesIdList: List<String>? = null

    var reviewId: Long? = null
    var themeId: Long? = null
    var x: Double? = null
    var y: Double? = null
    var name: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newThemeId = arguments?.getLong("THEME_ID")
        themesIdList = arguments?.getString("THEMES_ID_STRING")?.split(",")

        reviewId = arguments?.getLong("REVIEW_ID")
        themeId = arguments?.getLong("THEME_ID")
        x = arguments?.getDouble("PLACE_X")
        y = arguments?.getDouble("PLACE_Y")
        name = arguments?.getString("PLACE_NAME")

        val binding = DataBindingUtil.inflate<BottomSheetSelectNewThemeBinding>(
            inflater,
            R.layout.bottom_sheet_select_new_theme,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner

            actionEnroll.setOnClickListener {
                GlobalScope.launch { reviewInThemeViewModel.getAllReviews(x ?: NO_X_VALUE, y ?: NO_Y_VALUE, name ?: NO_PLACE_NAME) }
                dismiss()
            }
        }

        val adapter = MoveThemeAdapter().apply {
            setOnClickCardListener(object: OnClickCardListener {
                override fun onClick(themeId: Long, themeName: String) {
                    newThemeId = themeId
                }
            })
        }
        binding.listTheme.adapter = adapter

        viewModel.themeList.observe(requireActivity()) { themeList ->
            themeList.removeAt(0)
            for((index, theme) in themeList.withIndex()) {
                if(theme.id == themeId) {
                    adapter.selectedPosition = index
                    break
                }
            }
            adapter.submitList(themeList)
        }

        reviewInThemeViewModel.allReviewsList.observe(requireActivity()) { result: List<Review> ->
            val reviewChanged = makeReviewChanged(result[0])
            for(review in result) {
                reviewChanged?.let {
                    RetrofitProcedure.updateReviewOnlyTheme(review.id, themeId!!, it, UpdateReviewOnlyThemeType.TYPE_MOVE, newThemeId!!)
                }?:ToastUtil.toastShort("Error")
            }
            ToastUtil.toastShort("테마를 이동했습니다")
        }

        return binding.root
    }

    fun updateThemeIdInListAndReturnToString(): String {
        Log.i("THEME_ID_BEFORE", arguments?.getString("THEMES_ID_STRING").toString())
        val newList = themesIdList!!.filter { it != themeId.toString() && it != newThemeId.toString() }.map{ it.toLong() } as MutableList
        newList.add(newThemeId!!)
        newList.sort()

        val builder = StringBuilder()
        for((index, item) in newList.withIndex()) {
            builder.append(item.toString())
            if(index != newList.size-1) {
                builder.append(",")
            }
        }
        Log.i("THEME_ID_AFTER", builder.toString())
        return builder.toString()
    }

    fun makeReviewChanged(review: Review): ReviewChanged? {
        val place = PlaceWhenChangeReview(review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.address_name, review.place_name, review.phone)
        val themeIds = updateThemeIdInListAndReturnToString()
        return ReviewChanged(
            review.isPublic,
            review.category,
            review.comment,
            review.visitedDayYmd,
            review.companions,
            review.toliets,
            review.priceRange,
            review.serviceQuality,
            review.revisit,
            themeIds,
            place
        )
    }
}
