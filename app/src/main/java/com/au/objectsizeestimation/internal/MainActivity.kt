package com.au.objectsizeestimation.internal

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.au.library_mvvm.ComposeBaseViewModelActivity
import com.au.library_ui_compose.theme.ObjectSizeEstimationAppTheme
import com.au.library_ui_compose.ui.CameraPreview
import com.au.objectsizeestimation.internal.MainViewStateBinding.Layout
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class MainActivity : ComposeBaseViewModelActivity<MainViewModel>(MainViewModel::class.java) {

    private lateinit var cameraExecutor: ExecutorService

    private val cameraPermissionRequest = registerForActivityResult(RequestPermission()) {
        viewModel.requestPermissionComplete()
    }

    override val content: @Composable () -> Unit = {
        viewModel.binding.observeAsState().value?.let{
            ObjectSizeEstimationAppTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    when(it.layout) {
                        is Layout.Permission -> {
                            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                        }

                        is Layout.Camera -> {
                            cameraExecutor = Executors.newSingleThreadExecutor()
                            Screen(
                                cameraExecutor = cameraExecutor,
                                layout = it.layout,
                            )

                            it.layout.listDetected?.forEach { classification ->
                                Canvas(
                                    modifier = Modifier.fillMaxSize(),
                                    onDraw = {
                                        drawRect(
                                            style = Stroke(width = 2f),
                                            topLeft = Offset(classification.left, classification.top),
                                            color = Color.Red,
                                            size = Size(classification.width, classification.height),
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

@Composable
private fun Screen(layout: Layout.Camera, cameraExecutor: ExecutorService) {
    CameraPreview(
        cameraExecutor = cameraExecutor,
        onImageAnalysis = layout.onImageAnalysis,
    )
}