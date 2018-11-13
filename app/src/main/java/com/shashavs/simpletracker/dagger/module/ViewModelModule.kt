package com.shashavs.simpletracker.dagger.module

import android.app.Application
import com.shashavs.simpletracker.dagger.scope.ActivityScope
import com.shashavs.simpletracker.firebase.AppFirebaseStorage
import com.shashavs.simpletracker.firebase.AppRealtimeReference
import com.shashavs.simpletracker.location.TrackerLocation
import com.shashavs.simpletracker.main.viewmodel.AppViewModelFactory
import com.shashavs.simpletracker.preferences.AppPreference
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule() {

    @ActivityScope
    @Provides
    fun getViewModelFactory(application: Application,
                            preferences: AppPreference,
                            trackerLocation: TrackerLocation,
                            realtimeDatabase: AppRealtimeReference,
                            firebaseStorage: AppFirebaseStorage
    ) = AppViewModelFactory(application, preferences, trackerLocation, realtimeDatabase, firebaseStorage)
}