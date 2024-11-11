package com.au.objectsizeestimation.internal

import androidx.lifecycle.ViewModel
import com.au.library_mvvm.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module internal abstract class MainViewModule {

    @Binds @IntoMap @ViewModelKey(MainViewModel::class)
    abstract fun viewModel(viewModel: MainViewModel): ViewModel
}