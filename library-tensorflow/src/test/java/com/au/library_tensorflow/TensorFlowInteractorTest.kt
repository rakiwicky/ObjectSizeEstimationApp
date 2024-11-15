package com.au.library_tensorflow

import android.graphics.Bitmap
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TensorFlowInteractorTest{

    private val tensorFlowObjectDetector = mock<TensorFlowObjectDetector>()

    private val tensorFlowInteractor = TensorFlowInteractor(
        tensorFlowObjectDetector
    )

    @Test fun `detectObjects - delegates to tensorFlowObjectDetector`() {
        val bitmap = mock<Bitmap>().also {
            tensorFlowInteractor.detectObjects(it)
        }

        verify(tensorFlowObjectDetector).detect(bitmap)
    }
}