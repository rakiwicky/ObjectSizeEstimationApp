package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import com.au.objectsizeestimation.internal.ui.Classification

internal data class MainViewStateBinding(
    val title: String,
    val layout: Layout,
    val overlay: Overlay,
) {

    sealed interface Layout {
        data object Permission : Layout

        data class Camera(
            val onImageAnalysis: (ImageProxy) -> Unit,
        ) : Layout
    }

    sealed interface Overlay {
        data class None(val message: String) : Overlay

        data class Results(
            val listDetected: List<Classification>,
        ) : Overlay
    }
}