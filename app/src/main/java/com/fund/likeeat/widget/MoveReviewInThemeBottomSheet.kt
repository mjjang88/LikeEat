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
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.BottomSheetSelectNewThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.utilities.UpdateReviewOnlyThemeType
import com.fund.likeeat.viewmodels.AllThemesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoveReviewInThemeBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    var newThemeId: Long? = null
    var themesIdList: List<String>? = null

    var reviewId: Long? = null
    var themeId: Long? = null
    var isPublic: Boolean? = null
    var category: String? = null
    var comment: String? = null
    var visitedDayYmd: String? = null
    var companions: String? = null
    var toliets: String? = null
    var priceRange: String? = null
    var serviceQuality: String? = null
    var revisit: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var address: String? = null
    var name: String? = null
    var phoneNumber: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newThemeId = arguments?.getLong("THEME_ID")
        themesIdList = arguments?.getString("THEMES_ID_STRING")?.split(",")

        reviewId = arguments?.getLong("REVIEW_ID")
        themeId = arguments?.getLong("THEME_ID")
        isPublic = arguments?.getBoolean("REVIEW_IS_PUBLIC")
        category = arguments?.getString("REVIEW_CATEGORY")
        comment = arguments?.getString("REVIEW_COMMENT")
        visitedDayYmd = arguments?.getString("REVIEW_VISITED_DAY_YMD")
        companions = arguments?.getString("REVIEW_COMPANIONS")
        toliets = arguments?.getString("REVIEW_TOILETS")
        priceRange = arguments?.getString("REVIEW_PRICE_RANGE")
        serviceQuality = arguments?.getString("REVIEW_SERVICE_QUALITY")
        revisit = arguments?.getString("REVIEW_REVISIT")
        lat = arguments?.getDouble("PLACE_LAT")
        lng = arguments?.getDouble("PLACE_LNG")
        address = arguments?.getString("PLACE_ADDRESS")
        name = arguments?.getString("PLACE_NAME")
        phoneNumber = arguments?.getString("PLACE_PHONE_NUMBER")

        val binding = DataBindingUtil.inflate<BottomSheetSelectNewThemeBinding>(
            inflater,
            R.layout.bottom_sheet_select_new_theme,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner

            actionEnroll.setOnClickListener {
                val reviewChanged = makeReviewChanged()?.let {
                    RetrofitProcedure.updateReviewOnlyTheme(reviewId!!, themeId!!, it, UpdateReviewOnlyThemeType.TYPE_MOVE, newThemeId!!)
                }?:ToastUtil.toastShort("Error")

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

    fun makeReviewChanged(): ReviewChanged? {
        lat?.let { latNotNull ->
            lng?.let { lngNotNull ->
                isPublic?.let { isPublicNotNull ->
                    val place = PlaceWhenChangeReview(latNotNull, lngNotNull, address, name, phoneNumber)
                    val themeIds = updateThemeIdInListAndReturnToString()
                    return ReviewChanged(
                        isPublicNotNull,
                        category,
                        comment,
                        visitedDayYmd,
                        companions,
                        toliets,
                        priceRange,
                        serviceQuality,
                        revisit,
                        themeIds,
                        place
                    )
                }
            }
        }?:return null
    }
}