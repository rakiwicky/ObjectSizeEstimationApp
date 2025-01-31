package com.au.library_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class ComposeBaseViewModelActivity<BVM : BaseViewModel>(
    private val viewModelClass: Class<BVM>
) : ComponentActivity() {

    protected lateinit var viewModel: BVM

    protected abstract val content: @Composable () -> Unit

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]

        (viewModel as? LifecycleObserver)?.let {
            lifecycle.addObserver(it)
        }

        setContent {
            Box(Modifier.safeDrawingPadding()) {
                content()
            }
        }
    }
}