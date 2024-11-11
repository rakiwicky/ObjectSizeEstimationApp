package com.au.objectsizeestimation

import android.app.Application
import com.au.objectsizeestimation.dagger.components.DaggerAppComponent
import com.au.objectsizeestimation.dagger.modules.AppContextModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class ObjectDetectorApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .appContextModule(AppContextModule(this))
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}