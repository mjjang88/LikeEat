package com.fund.likeeat.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*

object PermissionManager {

    val PERMISSIONS_REQUEST_CODE = 100
    val GPS_ENABLE_REQUEST_CODE = 4001
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    fun checkPermissionWhenOnCreate(activity: Activity) {
        if (checkLocationServicesStatus(activity)) {
            checkRunTimePermission(activity)
        } else {
            showDialogForLocationServiceSetting(activity)
        }
    }

    fun checkRunTimePermission(activity: Activity) {

        val hasFineLocationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (!(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(activity, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }

            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    fun checkLocationServicesStatus(context: Context): Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showDialogForLocationServiceSetting(activity: Activity) {

        MaterialAlertDialogBuilder(activity)
            .setTitle("위치 서비스 비활성화")
            .setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n위치 설정을 수정하시겠습니까?")
            .setCancelable(true)
            .setPositiveButton("설정", DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE)
            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .show()
    }
}