package com.au.objectsizeestimation.internal

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.au.library_mvvm.ComposeBaseViewModelActivity
import com.au.library_ui_compose.theme.ObjectSizeEstimationAppTheme
import com.au.library_ui_compose.ui.CameraPreview
import com.au.objectsizeestimation.internal.MainViewStateBinding.Layout
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class MainActivity :
    ComposeBaseViewModelActivity<MainViewModel>(MainViewModel::class.java) {

    private lateinit var cameraExecutor: ExecutorService

    private val cameraPermissionRequest = registerForActivityResult(RequestPermission()) {
        viewModel.requestPermissionComplete()
    }

    override val content: @Composable () -> Unit = {
        viewModel.binding.observeAsState().value?.let {
            Content(
                binding = it,
                cameraExecutor = Executors.newSingleThreadExecutor(),
                cameraPermissionRequest = cameraPermissionRequest
            )
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
private fun Content(
    binding: MainViewStateBinding,
    cameraExecutor: ExecutorService,
    cameraPermissionRequest: ActivityResultLauncher<String>
) {
    ObjectSizeEstimationAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when (binding.layout) {
                is Layout.Permission -> {
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }

                is Layout.Camera -> {
                    Camera(
                        cameraExecutor = cameraExecutor,
                        layout = binding.layout,
                    )

                    binding.layout.listDetected?.forEach { classification ->
                        Canvas(
                            modifier = Modifier.fillMaxSize(),
                            onDraw = {
                                drawRect(
                                    color = Color.Red,
                                    topLeft = Offset(
                                        x = classification.boundingBox.left,
                                        y = classification.boundingBox.top,
                                    ),
                                    size = Size(
                                        classification.boundingBox.width(),
                                        classification.boundingBox.height()
                                    ),
                                    style = Stroke(
                                        width = 10f,
                                    )
                                )
                            }
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Color.White)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Text(
                                text = classification.label,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Red,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = classification.score.toString(),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Red,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Camera(layout: Layout.Camera, cameraExecutor: ExecutorService) {
    CameraPreview(
        cameraExecutor = cameraExecutor,
        onImageAnalysis = layout.onImageAnalysis,
    )
}