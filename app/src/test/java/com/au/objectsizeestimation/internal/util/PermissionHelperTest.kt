package com.au.objectsizeestimation.internal.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class PermissionHelperTest {

    private val context = mock<Context>()
    private val permissionHelper = PermissionHelper(context)

    @Config(sdk = [Build.VERSION_CODES.M])
    @Test fun `hasCameraPermission - permission is granted and sdk is greater than 23 - returns true`() {
        whenever(context.checkSelfPermission(Manifest.permission.CAMERA)).thenReturn(PackageManager.PERMISSION_GRANTED)

        val result = permissionHelper.hasCameraPermission()

        assertThat(result).isTrue()
    }

    @Config(sdk = [Build.VERSION_CODES.M])
    @Test fun `hasCameraPermission - permission is not granted and sdk is greater than 23 - returns false`() {
        whenever(context.checkSelfPermission(Manifest.permission.CAMERA)).thenReturn(PackageManager.PERMISSION_DENIED)

        val result = permissionHelper.hasCameraPermission()

        assertThat(result).isFalse()
    }

    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    @Test fun `hasCameraPermission - permission is granted and sdk is less than 23 - returns true`() {
        whenever(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)).thenReturn(PackageManager.PERMISSION_GRANTED)

        val result = permissionHelper.hasCameraPermission()

        assertThat(result).isTrue()
    }

    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    @Test fun `hasCameraPermission - permission is not granted and sdk is less than 23 - returns false`() {
        whenever(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)).thenReturn(PackageManager.PERMISSION_DENIED)

        val result = permissionHelper.hasCameraPermission()

        assertThat(result).isFalse()
    }
}
