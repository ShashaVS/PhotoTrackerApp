package com.shashavs.simpletracker.main.viewmodel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.shashavs.simpletracker.firebase.AppFirebaseStorage
import com.shashavs.simpletracker.firebase.AppRealtimeReference
import com.shashavs.simpletracker.location.TrackerLocation
import com.shashavs.simpletracker.preferences.AppPreference
import javax.inject.Inject

class AppViewModelFactory @Inject constructor(val application: Application,
                                              val preferences: AppPreference,
                                              val trackerLocation: TrackerLocation,
                                              val realtimeDatabase: AppRealtimeReference,
                                              val firebaseStorage: AppFirebaseStorage
): ViewModelProvider.Factory  {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        AppViewModel(
            application,
            preferences,
            trackerLocation,
            realtimeDatabase,
            firebaseStorage
        ) as T
}