package com.au.library_mvvm.dagger

import androidx.lifecycle.ViewModelProvider
import com.au.library_mvvm.dagger.internal.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds abstract fun viewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}