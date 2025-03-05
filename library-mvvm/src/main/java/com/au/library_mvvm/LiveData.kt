package com.au.library_mvvm

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.getNonNullValue(): T {
    return this.value!!
}