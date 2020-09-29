package com.fund.likeeat.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.CardClickListener
import com.fund.likeeat.adapter.OnClickCardListener
import com.fund.likeeat.adapter.PlacesInThemeAdapter
import com.fund.likeeat.adapter.SearchPlaceAdapter
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ActivitySearchPlaceInThemeBinding
import com.fund.likeeat.manager.KeyboardManager
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.viewmodels.SEARCH_OPTION_ACCURACY
import com.fund.likeeat.viewmodels.SEARCH_OPTION_NEAR_CURRUNT_LOCATION
import com.fund.likeeat.viewmodels.SEARCH_OPTION_NEAR_MAP_LOCATION
import com.fund.likeeat.viewmodels.SearchPlaceInThemeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_search_place_in_theme.*
import kotlinx.coroutines.selects.select
import org.koin.android.ext.android.inject
import kotlin.math.pow

const val NOT_CREATED = -300L

class SearchPlaceInThemeActivity : AppCompatActivity() {
    private val searchPoiViewModel: SearchPlaceInThemeViewModel by inject()
    private lateinit var binding: ActivitySearchPlaceInThemeBinding

    val selectedSet = hashSetOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivitySearchPlaceInThemeBinding>(
            this,
            R.layout.activity_search_place_in_theme
        )

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
}