package com.fund.likeeat.ui

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
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.halfExpandedRatio = 0.45f
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.searchLayoutParent.setBackgroundColor(Color.WHITE)
                        binding.searchLayout.setBackgroundResource(R.drawable.item_border_gray)

                        binding.friendListButton.visibility = View.GONE
                        binding.scroll.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding.friendListButton.visibility = View.VISIBLE
                        binding.scroll.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.btnReviewListMe.visibility = View.VISIBLE
                        binding.friendListButton.visibility = View.VISIBLE
                        binding.scroll.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.searchLayoutParent.setBackgroundColor(Color.TRANSPARENT)
                        binding.searchLayout.setBackgroundResource(R.drawable.item_border_shadow)
                    }
                }
            }

        })

        val adapter = ReviewsAdapter()
        binding.recycler.adapter = adapter
        subscribeUi(adapter)

        binding.btnReviewListMe.setOnClickListener {
            binding.btnReviewListMe.visibility = View.GONE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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
            adapter.submitList(result)
        }
    }

}