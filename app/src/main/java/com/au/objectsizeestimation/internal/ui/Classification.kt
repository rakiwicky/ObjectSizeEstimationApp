package com.au.objectsizeestimation.internal.ui

import android.graphics.RectF

internal data class Classification(
    val label: String,
    val score: Float,
    val boundingBox: RectF,
)
