package com.fund.likeeat.manager

import com.fund.likeeat.R
import com.fund.likeeat.widget.*

enum class Category(val categoryName: String, val imageId: Int) {
    KoreanFood("한식", R.drawable.ic_frame_category_korea),
    ChineseFood("중식", R.drawable.ic_frame_category_china),
    JapaneseFood("일식", R.drawable.ic_frame_category_japan),
    WesternFood("양식", R.drawable.ic_frame_category_restaurant),
    AsianFood("아시안", R.drawable.ic_frame_category_asian),
    WorldFood("세계", R.drawable.ic_frame_category_world),
    SnackBar("분식", R.drawable.ic_frame_category_snack),
    Cafe("카페", R.drawable.ic_frame_category_cafe),
    FastFood("패스트푸드", R.drawable.ic_frame_category_fastfood);
}

enum class Evaluation(val evalName: String, val imageId: Int) {
    VeryGood("최고야", R.drawable.ic_frame_category_korea),
    Good("맛있어", R.drawable.ic_frame_category_korea),
    Soso("그냥그래", R.drawable.ic_frame_category_korea),
    Bad("실망이야", R.drawable.ic_frame_category_korea),
    VeryBad("이게뭐야", R.drawable.ic_frame_category_korea)
}

enum class Companions(val CompanionName: String, val imageId: Int) {
    Solo("혼자서", R.drawable.ic_frame_category_korea),
    Friends("친구와", R.drawable.ic_frame_category_korea),
    Parents("부모님과", R.drawable.ic_frame_category_korea),
    Lover("여친/남친", R.drawable.ic_frame_category_korea),
    Date("소개팅", R.drawable.ic_frame_category_korea),
    Company("회식에서", R.drawable.ic_frame_category_korea)
}

enum class Price(val priceName: String, val imageId: Int) {
    Less10000("만원이하", R.drawable.ic_frame_category_korea),
    More10000("만원~", R.drawable.ic_frame_category_korea),
    More20000("2만원~", R.drawable.ic_frame_category_korea),
    More30000("3만원~", R.drawable.ic_frame_category_korea),
    More50000("5만원~", R.drawable.ic_frame_category_korea),
    More100000("10만원~", R.drawable.ic_frame_category_korea)
}

enum class Toilet(val toiletName: String, val imageId: Int) {
    Clean("깨끗해", R.drawable.ic_frame_category_korea),
    Dirty("더러워", R.drawable.ic_frame_category_korea),
    Unisex("남여공용", R.drawable.ic_frame_category_korea),
    Insta("인스타각", R.drawable.ic_frame_category_korea)
}

enum class Revisit(val revisitName: String, val imageId: Int) {
    Always("재방문각", R.drawable.ic_frame_category_korea),
    Sometimes("근처오면", R.drawable.ic_frame_category_korea),
    Never("다신안와", R.drawable.ic_frame_category_korea)
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

fun getToiletImageByName(name: String): Int {
    return when (name) {
        "깨끗해" -> Toilet.Clean.imageId
        "더러워" -> Toilet.Dirty.imageId
        "남여공용" -> Toilet.Unisex.imageId
        "인스타각" -> Toilet.Insta.imageId
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