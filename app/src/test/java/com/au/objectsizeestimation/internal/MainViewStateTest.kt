package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.au.library.android.test.applicationResources
import com.au.library_mvvm.getNonNullValue
import com.au.objectsizeestimation.internal.MainViewState.OverlayState
import com.google.common.truth.Truth.assertThat
import com.au.objectsizeestimation.internal.MainViewState.TargetState
import com.au.objectsizeestimation.internal.MainViewStateBinding.Layout
import com.au.objectsizeestimation.internal.MainViewStateBinding.Overlay
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
                layout = Layout.Permission,
                overlay = Overlay.None(
                    message = "Nothing detected!"
                )
            )
        )
    }

    @Test fun `moveTo - permission state - verify`() {
        viewState.moveTo(TargetState.Permission)

        assertThat(viewState.binding.getNonNullValue().layout).isEqualTo(
            Layout.Permission
        )
    }

    @Test fun `moveTo - camera state - verify`() {
        viewState.moveTo(TargetState.Camera(onImageAnalysis))

        assertThat(viewState.binding.getNonNullValue().layout).isEqualTo(
            Layout.Camera(
                onImageAnalysis = onImageAnalysis
            )
        )
    }

    @Test fun `setOverlayState - none state - verify`() {
        viewState.setOverlayState(OverlayState.None)

        assertThat(viewState.binding.getNonNullValue().overlay).isEqualTo(
            Overlay.None(
                message = "Nothing detected!"
            )
        )
    }

    @Test fun `setOverlayState - results state - verify`() {
        viewState.setOverlayState(OverlayState.Results(
            listDetected = listDetected
        ))

        assertThat(viewState.binding.getNonNullValue().overlay).isEqualTo(
            Overlay.Results(
                listDetected = listDetected
            )
        )
    }
}