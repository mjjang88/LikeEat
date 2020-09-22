package com.fund.likeeat.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.PlaceListAdapter
import com.fund.likeeat.databinding.ActivitySearchPlaceBinding
import com.fund.likeeat.manager.KeyboardManager
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.viewmodels.SEARCH_OPTION_ACCURACY
import com.fund.likeeat.viewmodels.SEARCH_OPTION_NEAR_CURRUNT_LOCATION
import com.fund.likeeat.viewmodels.SEARCH_OPTION_NEAR_MAP_LOCATION
import com.fund.likeeat.viewmodels.SearchPlaceViewModel
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_search_place.*
import kotlinx.android.synthetic.main.item_reviews.*
import org.koin.android.ext.android.inject

class SearchPlaceActivity : AppCompatActivity() {

    private val searchPoiViewModel: SearchPlaceViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySearchPlaceBinding>(
            this,
            R.layout.activity_search_place
        )

        initList()
        initEditText()
        initChip()

        intent.getParcelableExtra<LatLng>(INTENT_KEY_LOCATION).apply {
            searchPoiViewModel.locate.value = this
        }

        btn_back.setOnClickListener {
            finish()
        }
    }

    private fun initList() {
        val adapter = PlaceListAdapter()
        list_place.adapter = adapter

        searchPoiViewModel.placeList.observe(this) {
            adapter.submitList(it.toList())
        }
    }

    private fun initEditText() {
        edit_search.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchPoiViewModel.searchWord.value = v.text.toString()
                    searchPoiViewModel.searchOption.value = radio_group_filter.checkedChipId.let {
                        when (it) {
                            chip_distance_by_location.id -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                            chip_distance_by_map.id -> SEARCH_OPTION_NEAR_MAP_LOCATION
                            chip_match.id -> SEARCH_OPTION_ACCURACY
                            else -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                        }
                    }
                    searchPoiViewModel.getPlaceList()
                    v.clearFocus()
                    KeyboardManager.hideKeyboard(this, v)
                    true
                }
                else -> false
            }
        }
    }

    private fun initChip() {
        radio_group_filter.setOnCheckedChangeListener { group, checkedId ->
            if (searchPoiViewModel.searchWord.value.isNullOrBlank()) {
                return@setOnCheckedChangeListener
            }

            searchPoiViewModel.searchOption.value = when (checkedId) {
                chip_distance_by_location.id -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
                chip_distance_by_map.id -> SEARCH_OPTION_NEAR_MAP_LOCATION
                chip_match.id -> SEARCH_OPTION_ACCURACY
                else -> SEARCH_OPTION_NEAR_CURRUNT_LOCATION
            }

            searchPoiViewModel.getPlaceList()
            edit_search.clearFocus()
            KeyboardManager.hideKeyboard(this, edit_search)
        }
    }
}