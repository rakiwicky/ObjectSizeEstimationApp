package com.au.objectsizeestimation.dagger.components

import com.au.objectsizeestimation.ObjectDetectorApplication
import com.au.objectsizeestimation.dagger.modules.AppCommonModules
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules =  [ AppCommonModules::class ]
)
interface AppComponent: AndroidInjector<ObjectDetectorApplication>