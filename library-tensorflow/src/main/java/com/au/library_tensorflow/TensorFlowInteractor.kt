package com.au.library_tensorflow

import android.graphics.Bitmap
import org.tensorflow.lite.task.vision.detector.Detection
import javax.inject.Inject

class TensorFlowInteractor @Inject internal constructor(
    private val tensorFlowObjectDetector: TensorFlowObjectDetector
) {

    fun detectObjects(bitmap: Bitmap): List<Detection> {
        return tensorFlowObjectDetector.detect(bitmap)
    }
}