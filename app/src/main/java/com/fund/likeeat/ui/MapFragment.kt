package com.fund.likeeat.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.fund.likeeat.R
import com.fund.likeeat.databinding.FragmentMapBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.manager.PermissionManager
import com.fund.likeeat.utilities.GpsTracker
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.viewmodels.MapViewModel
import com.kakao.sdk.user.UserApiClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.navigation_left.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mNaverMap : NaverMap

    private val mapViewModel: MapViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    private var highlightMarker: Marker? = null

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

        PermissionManager.checkPermissionWhenOnCreate(requireActivity())

        binding.btnReviewAndMap.setOnClickListener {
            val intent = Intent(requireContext(), ReviewsActivity::class.java)
            startActivity(intent)
        }

        /*binding.btnReviewAndMap.setOnClickListener {
            if(isReviewListOpen) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }*/

        mapInit()

        binding.btnAddReview.setOnClickListener {
            val intent = Intent(requireContext(), SearchPlaceActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
        }
        binding.btnAddReviewHighlight.setOnClickListener {
            val intent = Intent(requireContext(), SearchPlaceActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
        }

        binding.btnCurrentLocation.setOnClickListener {
            val gpsTracker = GpsTracker(requireContext())
            moveToPoi(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
        }
        binding.btnCurrentLocationHighlight.setOnClickListener {
            val gpsTracker = GpsTracker(requireContext())
            moveToPoi(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
        }

        binding.navigationLeft.all_theme_layout.setOnClickListener {
            val intent = Intent(requireContext(), ThemeActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
            drawer.closeDrawer(GravityCompat.START)
        }

        binding.buttonLeftDrawer.setOnClickListener {
            dataInit()  //TODO 현재 로그인 안 된 상태에서도 MapFragment 진입하기 때문에 오류가 발생함 (그렇게 유추하고 있음). 해결되면 바로 밖으로 빼버리자
            drawer.openDrawer(GravityCompat.START)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PermissionManager.GPS_ENABLE_REQUEST_CODE -> {
                if (PermissionManager.checkLocationServicesStatus(requireContext())) {
                    PermissionManager.checkRunTimePermission(requireActivity())
                }
            }
        }
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
                marker.icon = OverlayImage.fromResource(R.drawable.marker_base)
                marker.setOnClickListener {
                    highlightMarker?.map = null
                    val highLightMarker = Marker()
                    highLightMarker.position = LatLng(review.y, review.x)
                    highLightMarker.icon = OverlayImage.fromResource(R.drawable.marker_base_highlight)
                    highLightMarker.map = mNaverMap
                    highlightMarker = highLightMarker
                    changeState(STATE_HIGHLIGHT)

                    text_highlight_place_name.text = review.place_name
                    text_highlight_place_address.text = review.address_name

                    true
                }
                marker.map = mNaverMap
            }

            mapViewModel.getReviewFullList(it)
        }
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
        mNaverMap.setOnMapClickListener { pointF, latLng ->
            highlightMarker?.map = null
            changeState(STATE_NORMAL)
        }

        initAfterMapReady()
    }

    private fun initAfterMapReady() {
        subscribeUi()
    }

    val STATE_NORMAL = false
    val STATE_HIGHLIGHT = true
    private fun changeState(state: Boolean) {
        when (state) {
            STATE_NORMAL -> {
                layout_fab_none_highlight.visibility = View.VISIBLE
                layout_fab_highlight.visibility = View.GONE
                btn_review_and_map.visibility = View.VISIBLE
            }
            STATE_HIGHLIGHT -> {
                layout_fab_none_highlight.visibility = View.GONE
                layout_fab_highlight.visibility = View.VISIBLE
                btn_review_and_map.visibility = View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionManager.PERMISSIONS_REQUEST_CODE &&
            grantResults.size == PermissionManager.REQUIRED_PERMISSIONS.size) {

            var check_result = true

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }

            if (!check_result) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PermissionManager.REQUIRED_PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PermissionManager.REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(requireContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun moveToPoi(latLng: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(latLng)
            .animate(CameraAnimation.Linear)
            .finishCallback {
                mNaverMap.cameraPosition = CameraPosition(latLng, 16.0)
            }
        mNaverMap.moveCamera(cameraUpdate)
    }

    private fun getMyKakaoAccountInfoAndSetProfileView() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                ToastUtil.toastShort("사용자 정보 요청 실패")
            }
            else if (user != null) {
                navigation_left.profile_name.text = user.kakaoAccount?.profile?.nickname

                Glide.with(this)
                    .load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(navigation_left.profile_image)
            }
        }
    }

    // Left Drawer 열었을 때, 관련 데이터가 바로 보일 수 있도록, MapFragment 진입시 동작하게 했습니다.
    // 명준님께서 작업하신 기타 Init 작업이 있으시다면 같이 묶어도 괜찮습니다!
    private fun dataInit() {
        getMyKakaoAccountInfoAndSetProfileView()
    }
}