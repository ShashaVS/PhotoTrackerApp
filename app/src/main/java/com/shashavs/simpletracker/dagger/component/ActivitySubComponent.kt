package com.shashavs.simpletracker.dagger.component

import com.shashavs.simpletracker.dagger.module.CameraModule
import com.shashavs.simpletracker.dagger.module.ViewModelModule
import com.shashavs.simpletracker.dagger.scope.ActivityScope
import com.shashavs.simpletracker.fragments.TrackerMapFragment
import com.shashavs.simpletracker.fragments.detail.DetailFragment
import com.shashavs.simpletracker.main.MainActivity

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf( ViewModelModule::class, CameraModule::class ))
interface ActivitySubComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: TrackerMapFragment)
    fun inject(fragment: DetailFragment)
}

