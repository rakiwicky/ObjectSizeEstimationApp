package com.au.library_mvvm.dagger.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        providers[modelClass]?.let { return createViewModel(it) }

        providers.entries.firstOrNull{ modelClass.isAssignableFrom(it.key) }?.let {
            return createViewModel(it.value)
        }

        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : ViewModel> createViewModel(provider: Provider<ViewModel>): T {
        return provider.get() as T
    }
}