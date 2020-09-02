package com.fund.likeeat.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsAdapter
import com.fund.likeeat.databinding.FragmentMapBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mNaverMap : NaverMap

    private val mapViewModel: MapViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var isReviewListOpen = false
        val binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            viewModel = mapViewModel
        }
        context ?: return binding.root

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetViewReviewList).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.apply {
                        when (newState) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                searchLayoutParent.setBackgroundColor(Color.WHITE)
                                searchLayout.setBackgroundResource(R.drawable.item_border_round_gray)
                                bottomSheet.setBackgroundResource(R.drawable.item_border_top_gray)
                                //friendListButton.visibility = View.GONE
                                scroll.visibility = View.GONE
                                btnReviewAndMap.text = "지도 보기"
                                isReviewListOpen = true
                            }
                            else -> {
                                //friendListButton.visibility = View.VISIBLE
                                scroll.visibility = View.VISIBLE
                                bottomSheet.setBackgroundResource(R.drawable.item_border_top_round_shadow)
                                searchLayoutParent.setBackgroundColor(Color.TRANSPARENT)
                                searchLayout.setBackgroundResource(R.drawable.item_border_round_shadow)
                                btnReviewAndMap.text = "목록 보기"
                                isReviewListOpen = false
                            }
                        }
                    }
                }
            })
        }

        initReviewList(binding)

        binding.btnReviewAndMap.setOnClickListener {
            when (currentState) {
                STATE_MAP -> changeState(STATE_REVIEW_LIST)
                STATE_REVIEW_LIST -> changeState(STATE_MAP)
            }
        }

        /*binding.btnReviewAndMap.setOnClickListener {
            if(isReviewListOpen) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }*/

        mapInit()

        binding.btnAddReview.setOnClickListener {
            startActivity(Intent(requireContext(), SearchPlaceActivity::class.java))
        }

        binding.testButton.setOnClickListener {
            val intent = Intent(requireContext(), SetThemeActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun mapInit() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    private fun subscribeUi() {
        mapViewModel.review.observe(viewLifecycleOwner) {
            it.forEach {review ->
                val marker = Marker()
                marker.captionText = review.place_name ?: "Null"
                marker.position = LatLng(review.y!!, review.x!!)
                marker.map = mNaverMap
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0

        initAfterMapReady()
    }

    private fun initAfterMapReady() {
        subscribeUi()
    }

    private fun initReviewList(binding: FragmentMapBinding) {
        val adapter = ReviewsAdapter()
        binding.recyclerViewReviewList.adapter = adapter

        mapViewModel.reviewFull.observe(viewLifecycleOwner) { result ->
            // 거리 순 정렬을 어디서 어떻게 해야하지
            adapter.submitList(result)
        }

        mapViewModel.getReviewFullList()
    }

    val STATE_MAP = 0x000000
    val STATE_REVIEW_LIST = 0x000001
    val STATE_REVIEW_DETAIL = 0x000002
    var currentState = STATE_MAP
    private fun changeState(changeState: Int) {
        when (currentState) {
            STATE_MAP -> {
                when (changeState) {
                    STATE_REVIEW_LIST -> {
                        btn_review_and_map.text = "지도 보기"
                        layout_view_review_list.visibility = View.VISIBLE
                    }
                }
            }
            STATE_REVIEW_LIST -> {
                when (changeState) {
                    STATE_MAP -> {
                        btn_review_and_map.text = "목록 보기"
                        layout_view_review_list.visibility = View.GONE
                    }
                }
            }
        }

        currentState = changeState
    }
}