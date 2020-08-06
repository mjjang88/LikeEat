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
            btnReviewListMe.setOnClickListener {
                // 나의 목록만 보는 버튼이므로 지정되어있는 uid 값을 넘겨주기만 하면 된다.
                // (나중에 다른 사용자의 맛집 목록을 보는 경우에는 다른 방식으로 uid를 넘겨 주어야 함)
                val intent = Intent(activity, ReviewsActivity::class.java)
                intent.putExtra("uid", MyApplication.pref.uid)
                startActivity(intent)
            }

            btnReviewListFriend.setOnClickListener {
                val intent = Intent(activity, ReviewsActivity::class.java)
                intent.putExtra("uid", 4545454545)
                startActivity(intent)
            }
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
        mapViewModel.place.observe(viewLifecycleOwner, Observer {
            it.forEach {place ->
                val marker = Marker()
                marker.captionText = place.name
                marker.position = LatLng(place.y, place.x)
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