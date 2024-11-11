package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import com.au.objectsizeestimation.internal.ui.Classification

internal data class MainViewStateBinding(
    val title: String,
    val layout: Layout,
) {

    sealed interface Layout {
        data object Permission : Layout

        data class Camera(
            val onImageAnalysis: (ImageProxy) -> Unit,
            val listDetected: List<Classification>? = null,
        ) : Layout
    }
}