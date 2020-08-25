package com.fund.likeeat.network

import com.fund.likeeat.data.Place

class PlaceServerWrite(
    val lat: Double,
    val lng: Double,
    val address: String,
    val name: String,
    val phoneNumber: String
) {
    constructor(place: Place) : this(
        place.y,
        place.x,
        place.address.toString(),
        place.name.toString(),
        place.phone.toString()
    ) {
    }
}