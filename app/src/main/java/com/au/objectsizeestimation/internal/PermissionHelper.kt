package com.au.objectsizeestimation.internal

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

internal class PermissionHelper @Inject constructor(
    private val context: Context,
) {

    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
}