package com.au.objectsizeestimation.dagger.modules

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.au.objectsizeestimation.ObjectDetectorApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppContextModule(private val application: ObjectDetectorApplication) {

    @Provides @Singleton fun provideApplication(): Application = application

    @Provides @Singleton fun provideContext(): Context = application.applicationContext

    @Provides @Singleton fun provideResources(): Resources = application.resources
}