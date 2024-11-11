package com.au.library_mvvm

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class ComposeBaseViewModelActivity<BVM: BaseViewModel>(
    private val viewModelClass: Class<BVM>
): ComponentActivity() {

    protected lateinit var viewModel: BVM

    protected abstract val content: @Composable () -> Unit

    //@Composable abstract fun BuildContent()

    @Inject internal lateinit var viewModelFactory: ViewModelProvider.Factory

    /*override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                content()
            }
        }
    }*/

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