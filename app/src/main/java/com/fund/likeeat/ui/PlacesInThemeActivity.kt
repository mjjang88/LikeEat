package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.CardLongClickListener
import com.fund.likeeat.adapter.ReviewsInThemeAdapter
import com.fund.likeeat.databinding.ActivityPlacesInThemeBinding
import com.fund.likeeat.utilities.INTENT_KEY_LOCATION
import com.fund.likeeat.utilities.NO_PLACE_NAME
import com.fund.likeeat.utilities.NO_X_VALUE
import com.fund.likeeat.utilities.NO_Y_VALUE
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.fund.likeeat.widget.SetPlaceInThemeBottomSheet
import com.naver.maps.geometry.LatLng
import kotlinx.android.synthetic.main.activity_places_in_theme.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlacesInThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacesInThemeBinding
    val oneThemeViewModel: OneThemeViewModel by viewModel { parametersOf(intent.getLongExtra("THEME_ID", -12)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_places_in_theme)
        binding.apply {
            lifecycleOwner = this@PlacesInThemeActivity
            themeViewModel = oneThemeViewModel
            actionBack.setOnClickListener { finish() }
        }
        toolbar.title = ""
        setSupportActionBar(toolbar)

        val adapter = ReviewsInThemeAdapter().apply {
            setOnCardLongClickListener(object : CardLongClickListener {
                override fun onLongClick(reviewId: Long?, x: Double?, y: Double?, placeName: String?) {
                    val bundle = Bundle().apply {
                        putLong("REVIEW_ID", reviewId!!)
                        putDouble("PLACE_X", x ?: NO_X_VALUE)
                        putDouble("PLACE_Y", y ?: NO_Y_VALUE)
                        putString("PLACE_NAME", placeName ?: NO_PLACE_NAME)
                        putLong("THEME_ID", intent.getLongExtra("THEME_ID", -12))
                    }
                    if(intent.getStringExtra("THEME_NAME") == resources.getString(R.string.theme_all)) {
                        /* todo 등록한 모든 맛집일 경우에 보여지는 하단 bottomsheet는 다르다. (삭제 기능만 있을거임)
                            특별한 디자인 나온게 없어서, 보류
                        */

                    } else {
                        val dialog =
                            SetPlaceInThemeBottomSheet()
                        dialog.arguments = bundle
                        dialog.show(supportFragmentManager, dialog.tag)
                    }
                }
            })

        }
        recycler.adapter = adapter

        oneThemeViewModel.reviewIdList.observe(this) { result ->
            val reviewIdList = result.map { it.reviewId }

            GlobalScope.launch { oneThemeViewModel.getReviewsNoSameData(reviewIdList) }
        }

        oneThemeViewModel.reviewOneThemeNoSameData.observe(this) { result ->
            adapter.submitList(result)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_reviews_in_theme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_review -> {
                val intent = Intent(this@PlacesInThemeActivity, SearchPlaceInThemeActivity::class.java)
                intent.putExtra("THEME_ID", this.intent.getLongExtra("THEME_ID", -12))
                intent.putExtra(INTENT_KEY_LOCATION, this.intent.getParcelableExtra<LatLng>(INTENT_KEY_LOCATION))
                startActivity(intent)
            }
        }
        return true

    }
}