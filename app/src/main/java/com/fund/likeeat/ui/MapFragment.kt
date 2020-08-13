package com.fund.likeeat.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.ReviewsAdapter
import com.fund.likeeat.databinding.FragmentMapBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.DataUtils
import com.fund.likeeat.viewmodels.MapViewModel
import com.fund.likeeat.viewmodels.ReviewsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mNaverMap : NaverMap

    private val mapViewModel: MapViewModel by inject()
    private val reviewViewModel: ReviewsViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            viewModel = mapViewModel
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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
                                friendListButton.visibility = View.GONE
                                scroll.visibility = View.GONE
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                btnReviewListMe.hide()
                                friendListButton.visibility = View.VISIBLE
                            }
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                btnReviewListMe.show()
                                friendListButton.visibility = View.VISIBLE
                            }
                            else -> {
                                scroll.visibility = View.VISIBLE
                                bottomSheet.setBackgroundResource(R.drawable.item_border_top_round_shadow)
                                searchLayoutParent.setBackgroundColor(Color.TRANSPARENT)
                                searchLayout.setBackgroundResource(R.drawable.item_border_round_shadow)
                            }
                        }
                    }
                }
            })
        }

        val adapter = ReviewsAdapter()
        binding.recycler.adapter = adapter
        subscribeUi(adapter)

        binding.btnReviewListMe.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

       /* binding.btnReviewListFriend.setOnClickListener {
            val intent = Intent(activity, ReviewsActivity::class.java)
            intent.putExtra("uid", 4545454545)
            startActivity(intent)
        }*/

        context ?: return binding.root

        mapInit()

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

    /*private fun subscribeUi() {
        mapViewModel.review.observe(viewLifecycleOwner, Observer {
            it.forEach {review ->
                val marker = Marker()
                marker.captionText = review.name
                marker.position = LatLng(review.y, review.x)
                marker.map = mNaverMap
            }
        })
    }*/

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0

        initAfterMapReady()
    }

    private fun initAfterMapReady() {
        // subscribeUi()
    }

    private fun subscribeUi(adapter: ReviewsAdapter) {
        reviewViewModel.review?.observe(viewLifecycleOwner) { result ->
            // 거리 순 정렬을 어디서 어떻게 해야하지
            adapter.submitList(result)
        }
    }

}