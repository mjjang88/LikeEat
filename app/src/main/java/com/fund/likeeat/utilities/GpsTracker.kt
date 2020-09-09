package com.fund.likeeat.utilities

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.ContextCompat
import java.lang.Exception

class GpsTracker(
    context: Context
) : Service(), LocationListener {

    val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10.0F
    val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1

    var longitude: Double? = null
    var latitude: Double? = null
    var mLocation: Location? = null

    private var mContext: Context = context

    init {
        mLocation = getLocation()
    }

    fun getLocation() : Location? {
        var location: Location? = null

        try {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                return null
            }

            val hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (!(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)) {
                return null
            }

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                location?.let {
                    longitude = it.longitude
                    latitude = it.latitude
                }
            }

            if (isGpsEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    location?.let {
                        longitude = it.longitude
                        latitude = it.latitude
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun getLongitude(): Double {
        val longitude = mLocation?.let {
            it.longitude
        }
        return longitude?: 0.0
    }

    fun getLatitude(): Double {
        val latitude = mLocation?.let {
            it.latitude
        }
        return latitude?: 0.0
    }

    fun stopUsingGps() {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(p0: Location?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }
}