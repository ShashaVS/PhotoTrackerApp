package com.shashavs.simpletracker.dagger.component

import com.shashavs.simpletracker.dagger.module.*
import com.shashavs.simpletracker.dagger.scope.ActivityScope
import com.shashavs.simpletracker.dagger.scope.AppScope
import com.shashavs.simpletracker.main.App
import dagger.Component

@AppScope
@Component(modules = arrayOf(
    AppModule::class,
    FireBaseModule::class,
    LocationModule::class
))

interface AppComponent {
    fun inject(app: App)
    fun subComponent(viewModelModule: ViewModelModule, cameraModule: CameraModule): ActivitySubComponent
}