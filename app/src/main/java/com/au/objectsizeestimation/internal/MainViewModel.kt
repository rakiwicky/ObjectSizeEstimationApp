package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import androidx.lifecycle.viewModelScope
import com.au.library_mvvm.BaseViewModel
import com.au.library_tensorflow.TensorFlowInteractor
import com.au.objectsizeestimation.internal.MainViewState.TargetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
    private val tensorFlowInteractor: TensorFlowInteractor,
    private val permissionHelper: PermissionHelper,
    private val detectionMapper: DetectionMapper,
    viewStateFactory: MainViewState.Factory,
) : BaseViewModel() {

    private val viewState = viewStateFactory.create()
    val binding = viewState.binding

    init {
        onStartCamera()
    }

    fun requestPermissionComplete() {
        if (hasCameraPermission()) {
            viewState.moveTo(
                TargetState.Camera(
                    onImageAnalysis = ::onImageAnalysis,
                )
            )
        }
    }

    private fun onStartCamera() {
        if (!hasCameraPermission()) {
            viewState.moveTo(TargetState.Permission)
            return
        }

        viewState.moveTo(
            TargetState.Camera(
                onImageAnalysis = ::onImageAnalysis,
            )
        )
    }

    private fun onImageAnalysis(imageProxy: ImageProxy) {
        viewModelScope.launch {
            try {
                val listOfDetections = tensorFlowInteractor.detectObjects(imageProxy.toBitmap())

                withContext(Dispatchers.Main) {
                    viewState.moveTo(
                        TargetState.Camera(
                            onImageAnalysis = ::onImageAnalysis,
                            listDetected = detectionMapper.create(listOfDetections)
                        )
                    )
                }
            } catch (e: Exception) {
                //TODO - handle error
            } finally {
                imageProxy.close()
            }
        }
    }

    private fun hasCameraPermission() = permissionHelper.hasCameraPermission()
}