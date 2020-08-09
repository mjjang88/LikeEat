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
import com.fund.likeeat.viewmodels.SearchPlaceViewModel
import kotlinx.android.synthetic.main.activity_search_place.*
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
                    searchPoiViewModel.getPlaceList()
                    v.clearFocus()
                    KeyboardManager.hideKeyboard(this, v)
                    true
                }
                else -> false
            }
        }
    }
}