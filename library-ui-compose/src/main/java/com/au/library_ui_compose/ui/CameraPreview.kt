package com.au.library_ui_compose.ui

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.Preview.Builder
import androidx.camera.lifecycle.ProcessCameraProvider
import java.util.concurrent.ExecutorService

@Composable
fun CameraPreview(
    cameraExecutor: ExecutorService,
    onImageAnalysis: (ImageProxy) -> Unit,
) {

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Builder().build()
    val previewView = remember { PreviewView(context) }

    val cameraxSelector = remember {
        CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setTargetResolution(android.util.Size(300, 300))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }.apply {
        setAnalyzer(cameraExecutor) { imageProxy ->
            onImageAnalysis(imageProxy)
        }
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraxSelector,
            preview,
            imageAnalysis
        )
        preview.surfaceProvider = previewView.surfaceProvider
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

