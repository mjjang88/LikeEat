package com.fund.likeeat.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.fund.likeeat.R
import com.fund.likeeat.adapter.FriendListAdapter
import com.fund.likeeat.adapter.MainThemeAdapter
import com.fund.likeeat.adapter.OnSelectNavCardListener
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityMapBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.manager.PermissionManager
import com.fund.likeeat.utilities.*
import com.fund.likeeat.viewmodels.AllThemesViewModel
import com.fund.likeeat.viewmodels.MapOneThemeViewModel
import com.fund.likeeat.viewmodels.MapViewModel
import com.kakao.sdk.user.UserApiClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.navigation_left.view.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mNaverMap : NaverMap
    private val markers = hashMapOf<Long, Pair<String?, Marker>>()
    private var nowSelectedThemeName: String? = null
    private var nowSelectedTheme: Theme? = null

    private val mapViewModel: MapViewModel by viewModel { parametersOf(MyApplication.pref.uid) }
    private val themeViewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }
    private val mapOneThemeViewModel: MapOneThemeViewModel by inject()

    private var highlightMarker: Marker? = null
    private var highlightReview: Review? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMapBinding>(
            this,
            R.layout.activity_map
        )

        PermissionManager.checkPermissionWhenOnCreate(this)

        binding.btnReviewAndMap.setOnClickListener {
            val intent = Intent(this, ReviewsActivity::class.java)
            intent.putExtra(INTENT_KEY_THEME, nowSelectedTheme)
            startActivity(intent)
        }

        /*binding.btnReviewAndMap.setOnClickListener {
            if(isReviewListOpen) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }*/

        mapInit()

        binding.btnAddReview.setOnClickListener {
            val intent = Intent(this, SearchPlaceActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
        }
        binding.btnAddReviewHighlight.setOnClickListener {
            val intent = Intent(this, SearchPlaceActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
        }

        binding.btnCurrentLocation.setOnClickListener {
            val gpsTracker = GpsTracker(this)
            moveToPoi(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
        }
        binding.btnCurrentLocationHighlight.setOnClickListener {
            val gpsTracker = GpsTracker(this)
            moveToPoi(LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
        }

        binding.navigationLeft.all_theme_layout.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            intent.putExtra(INTENT_KEY_LOCATION, mNaverMap.cameraPosition.target)
            startActivity(intent)
            drawer.closeDrawer(GravityCompat.START)
        }

        binding.buttonLeftDrawer.setOnClickListener {
            dataInit()
            drawer.openDrawer(GravityCompat.START)
        }

        binding.navigationLeft.profile_setting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
            drawer.closeDrawer(GravityCompat.START)
        }

        binding.layoutPlaceInfo.setOnClickListener {
            val intent = Intent(this, ReviewDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_REVIEW, highlightReview)
            startActivity(intent)
        }

        binding.btnFriend.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }

        initFriend(binding)

        val adapter = MainThemeAdapter()
        adapter.setOnClickNavCardListener(object: OnSelectNavCardListener {
            override fun onSelectNavCard(theme: Theme) {
                adapter.selectedThemeId = theme.id
                binding.navigationLeft.theme_name.text = theme.name
                binding.navigationLeft.theme_count.text = resources.getString(R.string.review_count)
                    .replace("num", theme.reviewsCount.toString())
                binding.navigationLeft.theme_tag.setColorFilter(theme.color)
                binding.themeTitle.text = theme.name

                nowSelectedThemeName = theme.name
                nowSelectedTheme = theme

                mapOneThemeViewModel.getReviewIdListByThemeId(theme.id)
            }

        })

        binding.navigationLeft.all_theme_recycler.adapter = adapter
        themeViewModel.themeList.observe(this) { themeList ->
            try {
                if (adapter.selectedThemeId != themeList[adapter.selectedPosition].id) {
                    adapter.selectedPosition = 0
                }
            } catch(e: IndexOutOfBoundsException) {
                adapter.selectedPosition = 0
            }
            adapter.submitList(themeList)
            adapter.notifyItemChanged(adapter.selectedPosition)
        }

        mapOneThemeViewModel.reviewIdList.observe(this) { result ->
            val reviewIdList = result.map { it.reviewId }
            GlobalScope.launch { mapOneThemeViewModel.getReviews(reviewIdList) }
        }

        mapOneThemeViewModel.reviewOneTheme.observe(this) {
            markerAndStyleInit()
            if(nowSelectedThemeName != resources.getString(R.string.theme_all)) markerForTheme()
        }

        mapViewModel.placeCount.observe(this) {
            navigation_left.review_count.text = it.size.toString()
        }
    }

    fun initFriend(binding: ActivityMapBinding) {

        var isGetFriend = false
        var isGetFriendLink = false
        mapViewModel.kakaofriends.observe(this) {
            isGetFriend = true
            if (isGetFriend && isGetFriendLink) {
                mapViewModel.getFriendList()
            }
        }
        mapViewModel.friendLink.observe(this) {
            isGetFriendLink = true
            binding.navigationRight.textFriendCount.text = it.size.toString()
            if (isGetFriend && isGetFriendLink) {
                mapViewModel.getFriendList()
            }
        }

        val favoriteAdapter = FriendListAdapter()
        binding.navigationRight.listRightNaviFavorite.adapter = favoriteAdapter
        mapViewModel.favoriteFriends.observe(this) {
            favoriteAdapter.submitList(it)
        }

        val friendAdapter = FriendListAdapter()
        binding.navigationRight.listRightNaviFriends.adapter = friendAdapter
        mapViewModel.friends.observe(this) {
            friendAdapter.submitList(it)
        }

        binding.navigationRight.layoutRightNaviTitle.setOnClickListener {
            val intent = Intent(this, FriendsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PermissionManager.GPS_ENABLE_REQUEST_CODE -> {
                if (PermissionManager.checkLocationServicesStatus(this)) {
                    PermissionManager.checkRunTimePermission(this)
                }
            }
        }
    }

    private fun mapInit() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    private fun subscribeUi() {
        markers.clear()
        mapViewModel.review.observe(this) {
            it.forEach { review ->
                val marker = Marker()
                marker.captionText = review.place_name ?: "Null"
                marker.position = LatLng(review.y!!, review.x!!)
                marker.icon = setMarkerImageByCategory(review.category, TYPE_DEFAULT)
                marker.map = mNaverMap
                marker.setOnClickListener {
                    setHighLightReviewAndMarkerStyle(review)
                    changeState(STATE_HIGHLIGHT)
                    true
                }

                markers[review.id] = Pair(review.category, marker)
            }
            mapViewModel.getReviewFullList(it)

            val review = intent.getParcelableExtra<Review>(INTENT_KEY_REVIEW)
            review?.let {
                setHighLightReviewAndMarkerStyle(it)
                moveToPoi(LatLng(it.y?: 0.0, it.x?: 0.0))
                changeState(STATE_HIGHLIGHT)
            }
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionManager.REQUIRED_PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionManager.REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show()
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

    private fun dataInit() {
        getMyKakaoAccountInfoAndSetProfileView()
    }

    private fun markerAndStyleInit() {
        val iter = markers.keys.iterator()
        while(iter.hasNext()) {
            val id = iter.next()
            markers[id]?.second?.icon = setMarkerImageByCategory(markers[id]?.first, TYPE_DEFAULT)
        }
    }

    private fun markerForTheme() {
        mapOneThemeViewModel.reviewOneTheme.value?.forEach { review ->
            markers[review.id]?.second?.icon = setMarkerImageByCategory(markers[review.id]?.first, TYPE_THEME)
        }
    }

    private fun setHighLightReviewAndMarkerStyle(review: Review) {
        highlightMarker?.map = null
        val highLightMarker = Marker()
        highLightMarker.position = LatLng(review.y!!, review.x!!)
        highLightMarker.icon = setMarkerImageByCategory(review.category, TYPE_ACCENT)
        highLightMarker.map = mNaverMap
        highlightMarker = highLightMarker
        highlightReview = review

        text_highlight_place_name.text = review.place_name
        text_highlight_place_address.text = review.address_name
    }

    private fun setMarkerImageByCategory(category: String?, type: Int): OverlayImage {
        return when(type) {
            TYPE_DEFAULT -> {
                when(category) {
                    "한식" -> OverlayImage.fromResource(R.drawable.ic_korea_normal)
                    "중식" -> OverlayImage.fromResource(R.drawable.ic_china_normal)
                    "일식" -> OverlayImage.fromResource(R.drawable.ic_japan_normal)
                    "양식" -> OverlayImage.fromResource(R.drawable.ic_west_normal)
                    "아시안" -> OverlayImage.fromResource(R.drawable.ic_asian_normal)
                    "세계" -> OverlayImage.fromResource(R.drawable.ic_world_normal)
                    "분식" -> OverlayImage.fromResource(R.drawable.ic_snack_normal)
                    "카페" -> OverlayImage.fromResource(R.drawable.ic_cafe_normal)
                    "패스트푸드" -> OverlayImage.fromResource(R.drawable.ic_fastfood_normal)
                    else -> OverlayImage.fromResource(R.drawable.marker_base)
                }
            }
            TYPE_THEME -> {
                when(category) {
                    "한식" -> OverlayImage.fromResource(R.drawable.ic_korea_theme)
                    "중식" -> OverlayImage.fromResource(R.drawable.ic_china_theme)
                    "일식" -> OverlayImage.fromResource(R.drawable.ic_japan_theme)
                    "양식" -> OverlayImage.fromResource(R.drawable.ic_west_theme)
                    "아시안" -> OverlayImage.fromResource(R.drawable.ic_asian_theme)
                    "세계" -> OverlayImage.fromResource(R.drawable.ic_world_theme)
                    "분식" -> OverlayImage.fromResource(R.drawable.ic_snack_theme)
                    "카페" -> OverlayImage.fromResource(R.drawable.ic_cafe_theme)
                    "패스트푸드" -> OverlayImage.fromResource(R.drawable.ic_fastfood_theme)
                    else -> OverlayImage.fromResource(R.drawable.marker_theme)
                }
            }
            TYPE_ACCENT -> {
                when(category) {
                    "한식" -> OverlayImage.fromResource(R.drawable.ic_korea_accent)
                    "중식" -> OverlayImage.fromResource(R.drawable.ic_china_accent)
                    "일식" -> OverlayImage.fromResource(R.drawable.ic_japan_accent)
                    "양식" -> OverlayImage.fromResource(R.drawable.ic_west_accent)
                    "아시안" -> OverlayImage.fromResource(R.drawable.ic_asian_accent)
                    "세계" -> OverlayImage.fromResource(R.drawable.ic_world_accent)
                    "분식" -> OverlayImage.fromResource(R.drawable.ic_snack_accent)
                    "카페" -> OverlayImage.fromResource(R.drawable.ic_cafe_accent)
                    "패스트푸드" -> OverlayImage.fromResource(R.drawable.ic_fastfood_accent)
                    else -> OverlayImage.fromResource(R.drawable.marker_base_highlight)
                }
            }
            else -> {
                when(category) {
                    "한식" -> OverlayImage.fromResource(R.drawable.ic_korea_small)
                    "중식" -> OverlayImage.fromResource(R.drawable.ic_china_small)
                    "일식" -> OverlayImage.fromResource(R.drawable.ic_japan_small)
                    "양식" -> OverlayImage.fromResource(R.drawable.ic_west_small)
                    "아시안" -> OverlayImage.fromResource(R.drawable.ic_asian_small)
                    "세계" -> OverlayImage.fromResource(R.drawable.ic_world_small)
                    "분식" -> OverlayImage.fromResource(R.drawable.ic_snack_small)
                    "카페" -> OverlayImage.fromResource(R.drawable.ic_cafe_small)
                    "패스트푸드" -> OverlayImage.fromResource(R.drawable.ic_fastfood_small)
                    else -> OverlayImage.fromResource(R.drawable.ic_base_small)
                }
            }
        }

    }

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_THEME = 1
        const val TYPE_ACCENT = 2
        const val TYPE_SMALL = 3
    }
}