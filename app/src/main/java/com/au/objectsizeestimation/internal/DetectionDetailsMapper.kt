package com.au.objectsizeestimation.internal

import com.au.objectsizeestimation.internal.ui.Classification
import org.tensorflow.lite.task.vision.detector.Detection
import javax.inject.Inject


internal class DetectionDetailsMapper @Inject constructor() {

    fun create(
        detectionList: List<Detection>,
    ): List<Classification> {

        return detectionList.filter { detection ->
            detection.categories.any { it.score >= 0.5f }
        }.mapNotNull { detection ->
            val category = detection.categories.firstOrNull() ?: return@mapNotNull null

            val label = category.label
            val score = category.score
            val boundingBox = detection.boundingBox

            Classification(label, score, boundingBox)
        }
    }
}