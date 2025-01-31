package com.au.objectsizeestimation.internal

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.au.library.android.test.applicationResources
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewStateTest {

    private val viewState = MainViewState(
        applicationResources
    )

    @Test
    fun `verify - initial state`() {
        val binding = viewState.binding.value!!
        assertThat(binding).isEqualTo(
            MainViewStateBinding(
                title = "Object Size Estimation App",
                layout = MainViewStateBinding.Layout.Permission
            )
        )
    }
}