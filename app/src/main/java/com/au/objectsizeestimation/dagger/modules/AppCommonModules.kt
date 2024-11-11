package com.au.objectsizeestimation.dagger.modules

import com.au.library_mvvm.dagger.ViewModelFactoryModule
import com.au.objectsizeestimation.AppModule
import dagger.Module
import dagger.android.AndroidInjectionModule

@Module(
    includes = [
        AndroidInjectionModule::class,
        AppContextModule::class,
        ViewModelFactoryModule::class,
        AppModule::class
    ]
)
class AppCommonModules