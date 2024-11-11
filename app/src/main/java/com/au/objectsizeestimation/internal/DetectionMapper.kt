package com.au.objectsizeestimation.internal

import com.au.objectsizeestimation.internal.ui.Classification
import org.tensorflow.lite.task.vision.detector.Detection
import javax.inject.Inject

internal class DetectionMapper @Inject constructor() {

    fun create(
        detectionList: List<Detection>,
    ): List<Classification> {
        return detectionList.map {
            Classification(
                it.boundingBox.width(),
                it.boundingBox.height(),
                it.boundingBox.left,
                it.boundingBox.top,
            )
        }
    }
}