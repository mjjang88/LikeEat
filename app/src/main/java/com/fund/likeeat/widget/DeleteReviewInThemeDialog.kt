package com.fund.likeeat.widget

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.data.Place
import com.fund.likeeat.data.PlaceWhenChangeReview
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.*
import com.fund.likeeat.viewmodels.ReviewInThemeViewModel
import kotlinx.android.synthetic.main.dialog_delete_review_in_theme.*
import kotlinx.android.synthetic.main.dialog_delete_review_in_theme.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

class DeleteReviewInThemeDialog(val dataBundle: Bundle) : DialogFragment() {
    val themesIdList = dataBundle.getString("THEMES_ID_STRING")?.split(",")

    var reviewId: Long? = dataBundle.getLong("REVIEW_ID")
    var themeId: Long? = dataBundle.getLong("THEME_ID")
    var x: Double? = dataBundle.getDouble("PLACE_X")
    var y: Double? = dataBundle.getDouble("PLACE_Y")
    var name: String? = dataBundle.getString("PLACE_NAME")

    private val reviewInThemeViewModel : ReviewInThemeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_delete_review_in_theme, container, false)

        /*Log.i("DELETE_THEME", themesIdList.toString())
        Log.i("DELETE_THEME", reviewId.toString())
        Log.i("DELETE_THEME", themeId.toString())
        Log.i("DELETE_THEME", x.toString())
        Log.i("DELETE_THEME", name.toString())*/

        view.delete_cancel.setOnClickListener { dismiss() }
        view.delete_ok.setOnClickListener {
            GlobalScope.launch { reviewInThemeViewModel.getAllReviews(x ?: NO_X_VALUE, y ?: NO_Y_VALUE, name ?: NO_PLACE_NAME) }
            dismiss()
        }

        reviewInThemeViewModel.allReviewsList.observe(viewLifecycleOwner) { result: List<Review> ->
            val reviewChanged = makeReviewChanged(result[0])
            for(review in result) {
                reviewChanged?.let {
                    RetrofitProcedure.updateReviewOnlyTheme(review.id, themeId!!, it, UpdateReviewOnlyThemeType.TYPE_DELETE)
                }?:ToastUtil.toastShort("Error")
            }
            ToastUtil.toastShort("맛집을 테마에서 제거했습니다")
        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window?.attributes = layoutParams*/


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


    fun makeReviewChanged(review: Review): ReviewChanged? {
        val place = PlaceWhenChangeReview(review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.address_name, review.place_name, review.phone)
        val themeIds = deleteThemeIdInListAndReturnToString()
        return ReviewChanged(
            review.isPublic,
            review.category,
            review.comment,
            review.visitedDayYmd,
            review.companions,
            review.toliets,
            review.priceRange,
            review.serviceQuality,
            review.revisit,
            themeIds,
            place
        )
    }
}