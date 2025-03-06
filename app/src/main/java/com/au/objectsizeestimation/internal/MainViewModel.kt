package com.au.objectsizeestimation.internal

import androidx.camera.core.ImageProxy
import androidx.lifecycle.viewModelScope
import com.au.library_mvvm.BaseViewModel
import com.au.library_tensorflow.TensorFlowInteractor
import com.au.objectsizeestimation.internal.MainViewState.OverlayState
import com.au.objectsizeestimation.internal.MainViewState.TargetState
import com.au.objectsizeestimation.internal.mapper.DetectionDetailsMapper
import com.au.objectsizeestimation.internal.util.PermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MainViewModel @Inject constructor(
    private val tensorFlowInteractor: TensorFlowInteractor,
    private val permissionHelper: PermissionHelper,
    private val detectionDetailsMapper: DetectionDetailsMapper,
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
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val bitmap = imageProxy.toBitmap()
                val listOfDetections = tensorFlowInteractor.detectObjects(bitmap)

                withContext(Dispatchers.Main) {
                    if (listOfDetections.isEmpty()) {
                        viewState.setOverlayState(OverlayState.None)
                    } else {
                        viewState.setOverlayState(
                            OverlayState.Results(
                                listDetected = detectionDetailsMapper.create(
                                    listOfDetections,
                                )
                            )
                        )
                    }
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