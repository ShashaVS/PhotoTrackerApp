package com.shashavs.simpletracker.dagger.module

import com.shashavs.simpletracker.camera.AppCamera
import com.shashavs.simpletracker.dagger.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class CameraModule() {

    @ActivityScope
    @Provides
    fun getAppCamera(): AppCamera = AppCamera()
}