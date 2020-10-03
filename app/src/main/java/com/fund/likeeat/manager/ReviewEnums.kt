package com.fund.likeeat.manager

import com.fund.likeeat.R
import com.fund.likeeat.widget.*

enum class Category(val categoryName: String, val imageId: Int, val smallImageId: Int) {
    KoreanFood("한식", R.drawable.ic_frame_category_korea, R.drawable.ic_category_korea),
    ChineseFood("중식", R.drawable.ic_frame_category_china, R.drawable.ic_category_china),
    JapaneseFood("일식", R.drawable.ic_frame_category_japan, R.drawable.ic_category_japan),
    WesternFood("양식", R.drawable.ic_frame_category_restaurant, R.drawable.ic_category_restaurant),
    AsianFood("아시안", R.drawable.ic_frame_category_asian, R.drawable.ic_category_asian),
    WorldFood("세계", R.drawable.ic_frame_category_world, R.drawable.ic_category_world),
    SnackBar("분식", R.drawable.ic_frame_category_snack, R.drawable.ic_category_snack),
    Cafe("카페", R.drawable.ic_frame_category_cafe, R.drawable.ic_category_cafe),
    FastFood("패스트푸드", R.drawable.ic_frame_category_fastfood, R.drawable.ic_category_fastfood);
}

enum class Evaluation(val evalName: String, val imageId: Int, val smallImageId: Int) {
    VeryGood("최고야", R.drawable.ic_best_selector, R.drawable.ic_best_small),
    Good("맛있어", R.drawable.ic_delicious_selector, R.drawable.ic_delicious_small),
    Soso("그냥그래", R.drawable.ic_normal_selector, R.drawable.ic_normal_small),
    Bad("실망이야", R.drawable.ic_bad_selector, R.drawable.ic_bad_small),
    VeryBad("이게뭐야", R.drawable.ic_curious_selector, R.drawable.ic_curious_small)
}

enum class Companions(val CompanionName: String, val imageId: Int, val smallImageId: Int) {
    Solo("혼자서", R.drawable.ic_alone_selector, R.drawable.ic_alone_small),
    Friends("친구와", R.drawable.ic_friend_selector, R.drawable.ic_friend_small),
    Parents("부모님과", R.drawable.ic_parent_selector, R.drawable.ic_parent_small),
    Lover("여친/남친", R.drawable.ic_couple_selector, R.drawable.ic_couple_small),
    Date("소개팅", R.drawable.ic_date_selector, R.drawable.ic_date_small),
    Company("회식에서", R.drawable.ic_company_selector, R.drawable.ic_company_small)
}

enum class Price(val priceName: String, val imageId: Int, val smallImageId: Int) {
    Less10000("만원이하", R.drawable.ic_under1_selector, R.drawable.ic_under1_small),
    More10000("만원~", R.drawable.ic_1_selector, R.drawable.ic_1_small),
    More20000("2만원~", R.drawable.ic_2_selector, R.drawable.ic_2_small),
    More30000("3만원~", R.drawable.ic_3_selector, R.drawable.ic_3_small),
    More50000("5만원~", R.drawable.ic_5_selector, R.drawable.ic_5_small),
    More100000("10만원~", R.drawable.ic_10_selector, R.drawable.ic_10_small)
}

enum class Toilet(val toiletName: String, val imageId: Int, val smallImageId: Int) {
    Clean("깨끗해", R.drawable.ic_clean_selector, R.drawable.ic_clean_small),
    Dirty("더러워", R.drawable.ic_dirty_selector, R.drawable.ic_dirty_small),
    Unisex("남여공용", R.drawable.ic_unisex_selector, R.drawable.ic_unisex_small),
    Insta("인스타각", R.drawable.ic_sns_selector, R.drawable.ic_sns_small)
}

enum class Revisit(val revisitName: String, val imageId: Int, val smallImageId: Int) {
    Always("재방문각", R.drawable.ic_revisit_selector, R.drawable.ic_revisit_small),
    Sometimes("근처오면", R.drawable.ic_nearby_selector, R.drawable.ic_nearby_small),
    Never("다신안와", R.drawable.ic_never_selector, R.drawable.ic_never_small)
}

fun getCategoryImageByName(name: String): Int {
    return when (name) {
        "한식" -> Category.KoreanFood.imageId
        "중식" -> Category.ChineseFood.imageId
        "일식" -> Category.JapaneseFood.imageId
        "양식" -> Category.WesternFood.imageId
        "아시안" -> Category.AsianFood.imageId
        "세계" -> Category.WorldFood.imageId
        "분식" -> Category.SnackBar.imageId
        "카페" -> Category.Cafe.imageId
        "패스트푸드" -> Category.FastFood.imageId
        else -> -1
    }
}

fun getCategorySmallImageByName(name: String): Int {
    return when (name) {
        "한식" -> Category.KoreanFood.smallImageId
        "중식" -> Category.ChineseFood.smallImageId
        "일식" -> Category.JapaneseFood.smallImageId
        "양식" -> Category.WesternFood.smallImageId
        "아시안" -> Category.AsianFood.smallImageId
        "세계" -> Category.WorldFood.smallImageId
        "분식" -> Category.SnackBar.smallImageId
        "카페" -> Category.Cafe.smallImageId
        "패스트푸드" -> Category.FastFood.smallImageId
        else -> -1
    }
}

fun getEvaluationImageByName(name: String): Int {
    return when (name) {
        "최고야" -> Evaluation.VeryGood.imageId
        "맛있어" -> Evaluation.Good.imageId
        "그냥그래" -> Evaluation.Soso.imageId
        "실망이야" -> Evaluation.Bad.imageId
        "이게뭐야" -> Evaluation.VeryBad.imageId
        else -> -1
    }
}

fun getEvaluationSmallImageByName(name: String): Int {
    return when (name) {
        "최고야" -> Evaluation.VeryGood.smallImageId
        "맛있어" -> Evaluation.Good.smallImageId
        "그냥그래" -> Evaluation.Soso.smallImageId
        "실망이야" -> Evaluation.Bad.smallImageId
        "이게뭐야" -> Evaluation.VeryBad.smallImageId
        else -> -1
    }
}

fun getCompanionImageByName(name: String): Int {
    return when (name) {
        "혼자서" -> Companions.Solo.imageId
        "친구와" -> Companions.Friends.imageId
        "부모님과" -> Companions.Parents.imageId
        "여친/남친" -> Companions.Lover.imageId
        "소개팅" -> Companions.Date.imageId
        "회식에서" -> Companions.Company.imageId
        else -> -1
    }
}

fun getCompanionSmallImageByName(name: String): Int {
    return when (name) {
        "혼자서" -> Companions.Solo.smallImageId
        "친구와" -> Companions.Friends.smallImageId
        "부모님과" -> Companions.Parents.smallImageId
        "여친/남친" -> Companions.Lover.smallImageId
        "소개팅" -> Companions.Date.smallImageId
        "회식에서" -> Companions.Company.smallImageId
        else -> -1
    }
}

fun getPriceImageByName(name: String): Int {
    return when (name) {
        "만원이하" -> Price.Less10000.imageId
        "만원~" -> Price.More10000.imageId
        "2만원~" -> Price.More20000.imageId
        "3만원~" -> Price.More30000.imageId
        "5만원~" -> Price.More50000.imageId
        "10만원~" -> Price.More100000.imageId
        else -> -1
    }
}

fun getPriceSmallImageByName(name: String): Int {
    return when (name) {
        "만원이하" -> Price.Less10000.smallImageId
        "만원~" -> Price.More10000.smallImageId
        "2만원~" -> Price.More20000.smallImageId
        "3만원~" -> Price.More30000.smallImageId
        "5만원~" -> Price.More50000.smallImageId
        "10만원~" -> Price.More100000.smallImageId
        else -> -1
    }
}

fun getToiletImageByName(name: String): Int {
    return when (name) {
        "깨끗해" -> Toilet.Clean.imageId
        "더러워" -> Toilet.Dirty.imageId
        "남여공용" -> Toilet.Unisex.imageId
        "인스타각" -> Toilet.Insta.imageId
        else -> -1
    }
}

fun getToiletSmallImageByName(name: String): Int {
    return when (name) {
        "깨끗해" -> Toilet.Clean.smallImageId
        "더러워" -> Toilet.Dirty.smallImageId
        "남여공용" -> Toilet.Unisex.smallImageId
        "인스타각" -> Toilet.Insta.smallImageId
        else -> -1
    }
}

fun getRevisitImageByName(name: String): Int {
    return when (name) {
        "재방문각" -> Revisit.Always.imageId
        "근처오면" -> Revisit.Sometimes.imageId
        "다신안와" -> Revisit.Never.imageId
        else -> -1
    }
}

fun getRevisitSmallImageByName(name: String): Int {
    return when (name) {
        "재방문각" -> Revisit.Always.smallImageId
        "근처오면" -> Revisit.Sometimes.smallImageId
        "다신안와" -> Revisit.Never.smallImageId
        else -> -1
    }
}