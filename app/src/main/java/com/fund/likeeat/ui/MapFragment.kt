package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.fund.likeeat.R
import com.fund.likeeat.databinding.FragmentMapBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.viewmodels.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import org.koin.android.ext.android.inject

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mNaverMap : NaverMap

    private val mapViewModel: MapViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            viewModel = mapViewModel
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> binding.btnReviewListMe.visibility = View.GONE
                    else -> binding.btnReviewListMe.visibility = View.VISIBLE
                }
            }

        })

        binding.btnReviewListMe.setOnClickListener {
            /*val intent = Intent(activity, ReviewsActivity::class.java)
            intent.putExtra("uid", MyApplication.pref.uid)
            startActivity(intent)*/
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.btnReviewListFriend.setOnClickListener {
            val intent = Intent(activity, ReviewsActivity::class.java)
            intent.putExtra("uid", 4545454545)
            startActivity(intent)
        }

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
}