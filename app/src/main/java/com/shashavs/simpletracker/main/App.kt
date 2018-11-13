package com.shashavs.simpletracker.main

import android.app.Application
import com.shashavs.simpletracker.dagger.component.AppComponent
import com.shashavs.simpletracker.dagger.component.DaggerAppComponent
import com.shashavs.simpletracker.dagger.module.AppModule
import com.shashavs.simpletracker.dagger.module.FireBaseModule
import com.shashavs.simpletracker.dagger.module.LocationModule

class App: Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .fireBaseModule(FireBaseModule())
            .locationModule(LocationModule())
            .build()
    }

}