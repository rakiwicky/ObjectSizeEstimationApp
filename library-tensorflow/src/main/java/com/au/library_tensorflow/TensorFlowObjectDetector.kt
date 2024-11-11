package com.au.library_tensorflow

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject


internal class TensorFlowObjectDetector @Inject constructor(
    private val context: Context
) {
    private lateinit var objectDetector: ObjectDetector

    init {
        setupObjectDetector()
    }

    fun detect(image: Bitmap): List<Detection> {
        val tensorImage = TensorImage.fromBitmap(image)
        return objectDetector.detect(tensorImage)
    }

    private fun setupObjectDetector() {
        val baseOptions = BaseOptions.builder().setNumThreads(NUMBER_OF_THREADS).build()

        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(MAX_RESULTS)
            .setScoreThreshold(THRESHOLD)
            .build()

        objectDetector = ObjectDetector.createFromFileAndOptions(
            context,
            MODEL_PATH,
            options
        )
    }

    private companion object {
        const val THRESHOLD: Float = 0.5f
        const val NUMBER_OF_THREADS: Int = 2
        const val MAX_RESULTS: Int = 3
        const val MODEL_PATH: String = "ssd_mobilenet_v1.tflite"
    }
}