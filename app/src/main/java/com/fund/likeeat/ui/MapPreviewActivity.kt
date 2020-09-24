package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.R
import com.fund.likeeat.data.Place
import com.fund.likeeat.databinding.ActivityMapPreviewBinding
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.viewmodels.MapPreviewViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_map_preview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MapPreviewActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapPreviewViewModel: MapPreviewViewModel by inject()

    private lateinit var mNaverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMapPreviewBinding>(
            this,
            R.layout.activity_map_preview
        )

        initMap()

        btn_back.setOnClickListener {
            finish()
        }
    }

    fun initMap() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    fun initPlace() {
        val place = intent.getParcelableExtra<Place>(INTENT_KEY_PLACE)

        val marker = Marker()
        marker.captionText = place.name ?: "Null"
        marker.position = LatLng(place.y, place.x)
        marker.icon = OverlayImage.fromResource(R.drawable.marker_base_highlight)
        marker.map = mNaverMap

        moveToPoi(marker.position)

        text_highlight_place_name.text = place.name
        text_highlight_place_address.text = place.address

        btn_ok.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val review = mapPreviewViewModel.getReviewBySamePlace(place)

                withContext(Dispatchers.Main) {
                    if (review == null) {
                        val intent = Intent(this@MapPreviewActivity, AddReviewActivity::class.java)
                        intent.putExtra(INTENT_KEY_PLACE, place)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@MapPreviewActivity, ReviewDetailActivity::class.java)
                        intent.putExtra(INTENT_KEY_REVIEW, review)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0

        initPlace()
    }

    fun moveToPoi(latLng: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(latLng)
            .animate(CameraAnimation.Linear)
            .finishCallback {
                mNaverMap.cameraPosition = CameraPosition(latLng, 16.0)
            }
        mNaverMap.moveCamera(cameraUpdate)
    }
}