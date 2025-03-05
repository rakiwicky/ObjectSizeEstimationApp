package com.au.objectsizeestimation.internal

import android.content.res.Resources
import androidx.camera.core.ImageProxy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.au.library_mvvm.getNonNullValue
import com.au.objectsizeestimation.R
import com.au.objectsizeestimation.internal.MainViewStateBinding.Layout
import com.au.objectsizeestimation.internal.ui.Classification
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class MainViewState @AssistedInject constructor(
    resources: Resources,
) {

    private val _binding = MutableLiveData(
        MainViewStateBinding(
            title = resources.getString(R.string.app_name),
            layout = Layout.Permission
        )
    )

    val binding: LiveData<MainViewStateBinding> = _binding

    fun moveTo(targetState: TargetState) {
        val currentTargetState = _binding.getNonNullValue()

        _binding.value = when (targetState) {
            is TargetState.Permission -> currentTargetState.copy(
                layout = Layout.Permission
            )

            is TargetState.Camera -> currentTargetState.copy(
                layout = Layout.Camera(
                    onImageAnalysis = targetState.onImageAnalysis,
                    listDetected = targetState.listDetected,
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainViewState
    }

    sealed class TargetState {
        data object Permission : TargetState()

        data class Camera(
            val onImageAnalysis: (ImageProxy) -> Unit,
            val listDetected: List<Classification>? = null,
        ) : TargetState()
    }
}