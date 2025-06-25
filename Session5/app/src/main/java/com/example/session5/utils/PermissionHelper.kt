package com.example.session5.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper {
    companion object{
        const val CAMERA_PERMISSION_REQUEST_CODE = 100

        fun allPermissionsGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                   ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        fun requestCameraPermission(fragment: Fragment){
            fragment.requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }
}