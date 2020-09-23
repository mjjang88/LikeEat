package com.fund.likeeat.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.fund.likeeat.R
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceWhenChangeReview
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.ToastUtil
import kotlinx.android.synthetic.main.dialog_delete_review_in_theme.*

class DeleteReviewInThemeDialog(context: Context, val dataBundle: Bundle) : Dialog(context) {
    val themesIdList = dataBundle.getString("THEMES_ID_STRING")?.split(",")

    val reviewId = dataBundle.getLong("REVIEW_ID")
    val themeId = dataBundle.getLong("THEME_ID")
    val isPublic = dataBundle.getBoolean("REVIEW_IS_PUBLIC")
    val category = dataBundle.getString("REVIEW_CATEGORY")
    val comment = dataBundle.getString("REVIEW_COMMENT")
    val visitedDayYmd = dataBundle.getString("REVIEW_VISITED_DAY_YMD")
    val companions = dataBundle.getString("REVIEW_COMPANIONS")
    val toliets = dataBundle.getString("REVIEW_TOILETS")
    val priceRange = dataBundle.getString("REVIEW_PRICE_RANGE")
    val serviceQuality = dataBundle.getString("REVIEW_SERVICE_QUALITY")
    val revisit = dataBundle.getString("REVIEW_REVISIT")
    val lat = dataBundle.getDouble("PLACE_LAT")
    val lng = dataBundle.getDouble("PLACE_LNG")
    val address = dataBundle.getString("PLACE_ADDRESS")
    val name = dataBundle.getString("PLACE_NAME")
    val phoneNumber = dataBundle.getString("PLACE_PHONE_NUMBER")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window?.attributes = layoutParams

        setContentView(R.layout.dialog_delete_review_in_theme)
        delete_cancel.setOnClickListener { dismiss() }
        delete_ok.setOnClickListener {
            RetrofitProcedure.updateReviewOnlyTheme(reviewId, themeId, makeReviewChanged())
            dismiss()
        }
    }

    fun deleteThemeIdInListAndReturnToString(): String {
        val newList = themesIdList!!.filter { it != themeId.toString() }

        val builder = StringBuilder()
        for((index, item) in newList.withIndex()) {
            builder.append(item)
            if(index != newList.size-1) {
                builder.append(",")
            }
        }
        return builder.toString()
    }

    fun makeReviewChanged(): ReviewChanged {
        val place = PlaceWhenChangeReview(lat, lng, address, name, phoneNumber)
        val themeIds = deleteThemeIdInListAndReturnToString()
        return ReviewChanged(
            isPublic,
            category,
            comment,
            visitedDayYmd,
            companions,
            toliets,
            priceRange,
            serviceQuality,
            "왜 안들어가",
            themeIds,
            place
        )
    }
}