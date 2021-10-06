package com.skilled.weatherapp.util

import android.Manifest
import android.content.Context
import android.os.Build
import com.vmadalin.easypermissions.EasyPermissions

object LocationPermission {

    fun hasLocationPermission(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

//    fun requestLocationPermission(context: Context) {
//        EasyPermissions.requestPermissions(
//            context,
//            "This application cannot work without Location Permission.",
//            WeatherDataFragment.PERMISSION_LOCATION_REQUEST_CODE,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            SettingsDialog.Builder(this).build().show()
//            Toast.makeText(
//                this,
//                "Permission Denied!",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            requestLocationPermission()
//            Toast.makeText(
//                this,
//                "Permission Denied!",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//   override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
//        Toast.makeText(
//            this,
//            "Permission Granted!",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
}