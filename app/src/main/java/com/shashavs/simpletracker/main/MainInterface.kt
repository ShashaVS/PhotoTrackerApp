package com.shashavs.simpletracker.main

import android.support.v4.app.Fragment
import com.shashavs.simpletracker.dagger.component.ActivitySubComponent

interface MainInterface {
    fun activityComponent(): ActivitySubComponent?
    fun addFragment(tag: String, fragment: Fragment)
}