package com.fund.likeeat.ui

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.CardClickListener
import com.fund.likeeat.adapter.SearchPlaceAdapter
import com.fund.likeeat.data.PlaceWhenChangeReview
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.data.ReviewThemeLink
import com.fund.likeeat.databinding.ActivitySearchPlaceInThemeBinding
import com.fund.likeeat.manager.KeyboardManager
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.utilities.NO_X_VALUE
import com.fund.likeeat.utilities.NO_Y_VALUE
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.viewmodels.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_search_place_in_theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.pow

class SearchPlaceInThemeActivity : AppCompatActivity() {
    private val searchPoiViewModel: SearchPlaceInThemeViewModel by inject()
    private val addPlaceInThemeViewModel: AddPlaceInThemeViewModel by inject()

    private lateinit var binding: ActivitySearchPlaceInThemeBinding

    val selectedSet = hashSetOf<Review>()
    var themeId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySearchPlaceInThemeBinding>(
            this,
            R.layout.activity_search_place_in_theme
        )

        themeId = intent.getLongExtra("THEME_ID", -12)

        initEditText()
        initChip()

        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        intent.getParcelableExtra<LatLng>(INTENT_KEY_LOCATION).apply {
            searchPoiViewModel.locate.value = this
        }

        val adapter = SearchPlaceAdapter()
        adapter.setOnCardClickListener(object: CardClickListener {
            override fun onClick(review: Review) {
                if(review in selectedSet) {
                    selectedSet.remove(review)
                } else {
                    selectedSet.add(review)
                }
                searchPoiViewModel.selectedSet.value = selectedSet
            }
        })

        binding.listPlace.adapter = adapter

        searchPoiViewModel.selectedSet.observe(this) { result ->
            if(result.size == 0) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        searchPoiViewModel.reviewList.observe(this) { resultList ->

            if (resultList.isNotEmpty()) {
                image_suggest.visibility = View.GONE
                text_suggest.visibility = View.GONE
                list_place.visibility = View.VISIBLE
            } else {
                image_suggest.visibility = View.VISIBLE
                text_suggest.visibility = View.VISIBLE
                list_place.visibility = View.GONE
            }

            binding.apply {
                when(radioGroupFilter.checkedChipId) {
                    chipMatch.id -> { }
                    else -> {
                        resultList.sortBy {
                        (it.x!! - searchPoiViewModel.x.value!!).pow(2.0) + (it.y!! - searchPoiViewModel.y.value!!).pow(2.0) }
                    }
                }
            }

            adapter.submitList(resultList)
        }

        edit_search_button.setOnClickListener {
            searchPoiViewModel.searchWord.value = edit_search.text.toString()
            searchPoiViewModel.searchOption.value = binding.radioGroupFilter.checkedChipId.let {
                when (it) {
                    binding.chipDistanceByLocation.id -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                    binding.chipDistanceByMap.id -> SEARCH_OPTION_NEAR_MAP_LOCATION
                    binding.chipMatch.id -> SEARCH_OPTION_ACCURACY
                    else -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                }
            }
            searchPoiViewModel.search()
            binding.editSearch.clearFocus()
            KeyboardManager.hideKeyboard(this, binding.editSearch)
        }
        edit_delete.setOnClickListener { edit_search.text.clear() }


        binding.actionEnroll.setOnClickListener {
            val list: ArrayList<Review> = ArrayList(selectedSet)
            GlobalScope.launch {
                for(review in list) {
                    addPlaceInThemeViewModel.getRelations(review)
                    addPlaceInThemeViewModel.getReviewListByXYName(review)

                    /*for(item in addPlaceInThemeViewModel.reviewList.value!!) {

                    }*/
                }
            }
            ToastUtil.toastShort("맛집을 테마에 추가했습니다.")
            finish()
        }

        addPlaceInThemeViewModel.reviewList.observe(this) {result ->
            for(i in result) {
                val themeIdString = addPlaceInThemeViewModel.getThemeIdString()
                RetrofitProcedure.addReviewOnlyTheme(ReviewThemeLink(i.id, themeId!!), i.id, makeReviewChanged(i, themeIdString)!!)
            }
        }
    }

    private fun initEditText() {
        binding.editSearch.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchPoiViewModel.searchWord.value = v.text.toString()
                    searchPoiViewModel.searchOption.value = binding.radioGroupFilter.checkedChipId.let {
                        when (it) {
                            binding.chipDistanceByLocation.id -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                            binding.chipDistanceByMap.id -> SEARCH_OPTION_NEAR_MAP_LOCATION
                            binding.chipMatch.id -> SEARCH_OPTION_ACCURACY
                            else -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                        }
                    }
                    searchPoiViewModel.search()
                    v.clearFocus()
                    KeyboardManager.hideKeyboard(this, v)
                    true
                }
                else -> false
            }
        }
    }

    private fun initChip() {
        binding.radioGroupFilter.setOnCheckedChangeListener { group, checkedId ->
            if (searchPoiViewModel.searchWord.value.isNullOrBlank()) {
                return@setOnCheckedChangeListener
            }

            searchPoiViewModel.searchOption.value = when (checkedId) {
                binding.chipDistanceByLocation.id -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                binding.chipDistanceByMap.id -> SEARCH_OPTION_NEAR_MAP_LOCATION
                binding.chipMatch.id -> SEARCH_OPTION_ACCURACY
                else -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
            }

            searchPoiViewModel.search()
            binding.editSearch.clearFocus()
            KeyboardManager.hideKeyboard(this, binding.editSearch)
        }
    }

    fun updateThemeIdInListAndReturnToString(themeIdString: String?): String {
        val oldList = themeIdString!!.split(",")

        val newList = oldList.filter { it != themeId.toString() }.map{ it.toLong() } as MutableList
        newList.add(themeId!!)
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

    fun makeReviewChanged(review: Review, themeIdString: String?): ReviewChanged? {
        val place = PlaceWhenChangeReview(review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.address_name, review.place_name, review.phone)
        val themeIds = updateThemeIdInListAndReturnToString(themeIdString)
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