package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.au.library.android.test.applicationResources
import com.au.library_mvvm.getNonNullValue
import com.google.common.truth.Truth.assertThat
import com.au.objectsizeestimation.internal.MainViewState.TargetState
import com.au.objectsizeestimation.internal.ui.Classification
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MainViewStateTest {

    private val onImageAnalysis: (ImageProxy) -> Unit = mock()
    private val listDetected: List<Classification> = mock()

    private val viewState = MainViewState(
        applicationResources
    )

    @Test fun `verify - initial state`() {
        val binding = viewState.binding.getNonNullValue()
        assertThat(binding).isEqualTo(
            MainViewStateBinding(
                title = "Object Size Estimation App",
                layout = MainViewStateBinding.Layout.Permission
            )
        )
    }

    @Test fun `moveTo - permission state - verify`() {
        viewState.moveTo(TargetState.Permission)

        assertThat(viewState.binding.getNonNullValue().layout).isEqualTo(
            MainViewStateBinding.Layout.Permission
        )
    }

    @Test fun `moveTo - camera state with no image analysis results - verify`() {
        viewState.moveTo(TargetState.Camera(onImageAnalysis, null))

        assertThat(viewState.binding.getNonNullValue().layout).isEqualTo(
            MainViewStateBinding.Layout.Camera(
                onImageAnalysis = onImageAnalysis,
                listDetected = null
            )
        )
    }

    @Test fun `moveTo - camera state with image analysis results - verify`() {
        viewState.moveTo(TargetState.Camera(onImageAnalysis, listDetected))

        assertThat(viewState.binding.getNonNullValue().layout).isEqualTo(
            MainViewStateBinding.Layout.Camera(
                onImageAnalysis = onImageAnalysis,
                listDetected = listDetected
            )
        )
    }
}