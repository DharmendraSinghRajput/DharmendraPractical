package com.ssti.dharmendrapractical.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtil {

    private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2

    fun verifyPermissions(activity: Activity): Boolean {
        return if (!hasPermissions(activity, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            false
        } else
            true
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}