package com.au.objectsizeestimation

import com.au.objectsizeestimation.internal.MainActivity
import com.au.objectsizeestimation.internal.MainViewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class AppModule {

    @ContributesAndroidInjector(modules = [MainViewModule::class])
    internal abstract fun mainActivity() : MainActivity
}