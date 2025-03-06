package com.au.objectsizeestimation.internal

import android.graphics.RectF
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.au.library_tensorflow.TensorFlowInteractor
import com.au.objectsizeestimation.internal.MainViewState.OverlayState
import com.au.objectsizeestimation.internal.MainViewState.TargetState
import com.au.objectsizeestimation.internal.mapper.DetectionDetailsMapper
import com.au.objectsizeestimation.internal.ui.Classification
import com.au.objectsizeestimation.internal.util.PermissionHelper
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.argForWhich
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.detector.Detection

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    @get:Rule
    val dispatcherRule = object : TestWatcher() {
        override fun starting(description: Description?) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description?) {
            Dispatchers.resetMain()
        }
    }

    private val binding = mock<MainViewStateBinding>()

    private val viewState = mock<MainViewState> {
        on { binding } doReturn MutableLiveData(binding)
    }

    private val viewStateFactory = mock<MainViewState.Factory> {
        on { create() } doReturn viewState
    }

    private val tensorFlowInteractor = mock<TensorFlowInteractor>()
    private val permissionHelper = mock<PermissionHelper>()
    private val detectionDetailsMapper = mock<DetectionDetailsMapper>()

    private val viewModel by lazy {
        MainViewModel(
            tensorFlowInteractor = tensorFlowInteractor,
            permissionHelper = permissionHelper,
            detectionDetailsMapper = detectionDetailsMapper,
            viewStateFactory = viewStateFactory
        )
    }

    @Test
    fun `verify - initial state`() {
        assertThat(viewModel.binding.value).isEqualTo(binding)
    }

    @Test
    fun `init - with no camera permission - verify state`() {
        whenever(permissionHelper.hasCameraPermission()).thenReturn(false)

        viewModel

        inOrder(viewState) {
            verify(viewState).binding
            verify(viewState).moveTo(TargetState.Permission)
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `init - with camera permission - verify state`() {
        lateinit var onImageAnalysis: (ImageProxy) -> Unit
        whenever(permissionHelper.hasCameraPermission()).thenReturn(true)
        whenever(viewState.moveTo(argForWhich {
            (this as? TargetState.Camera)?.let {
                onImageAnalysis = it.onImageAnalysis
                true
            } == true
        })).thenAnswer {}

        viewModel

        inOrder(viewState) {
            verify(viewState).binding
            verify(viewState).moveTo(TargetState.Camera(onImageAnalysis))
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `init - with camera permission and no results detected - verify state`() = runTest {
        val imageProxy = mock<ImageProxy> {
            on { toBitmap() } doReturn mock()
        }

        lateinit var onImageAnalysis: (ImageProxy) -> Unit
        whenever(permissionHelper.hasCameraPermission()).thenReturn(true)
        whenever(viewState.moveTo(argForWhich {
            (this as? TargetState.Camera)?.let {
                onImageAnalysis = it.onImageAnalysis
                true
            } == true
        })).thenAnswer {}
        whenever(tensorFlowInteractor.detectObjects(any())).thenReturn(listOf<Detection>())

        viewModel

        onImageAnalysis(imageProxy)

        inOrder(viewState) {
            verify(viewState).binding
            verify(viewState).setOverlayState(
                OverlayState.None
            )
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `init - with camera permission and results detected - verify state`() = runTest {
        val imageProxy = mock<ImageProxy> {
            on { toBitmap() } doReturn mock()
        }
        val category1 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.5f
        }
        val category2 = mock<Category> {
            on { label } doReturn "label"
            on { score } doReturn 0.6f
        }
        val boundingBox1 = spy(RectF(1f, 2f, 3f, 4f))
        val boundingBox2 = spy(RectF(5f, 6f, 7f, 4f))
        val listDetected = mock<List<Detection>> {
            mock<Detection> {
                on { boundingBox } doReturn boundingBox1
                on { categories } doReturn listOf(category1)
            }
            mock<Detection> {
                on { boundingBox } doReturn boundingBox2
                on { categories } doReturn listOf(category2)
            }
        }

        lateinit var onImageAnalysis: (ImageProxy) -> Unit
        whenever(permissionHelper.hasCameraPermission()).thenReturn(true)
        whenever(viewState.moveTo(argForWhich {
            (this as? TargetState.Camera)?.let {
                onImageAnalysis = it.onImageAnalysis
                true
            } == true
        })).thenAnswer {}
        whenever(tensorFlowInteractor.detectObjects(any())).thenReturn(listDetected)
        val classificationList = listOf<Classification>().also {
            whenever(detectionDetailsMapper.create(eq(listDetected))).thenReturn(it)
        }

        viewModel

        onImageAnalysis(imageProxy)

        inOrder(viewState) {
            verify(viewState).binding
            verify(viewState).setOverlayState(
                OverlayState.Results(
                    listDetected = classificationList
                )
            )
            verifyNoMoreInteractions()
        }
    }
}