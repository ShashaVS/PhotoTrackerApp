package com.shashavs.simpletracker.dagger.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.shashavs.simpletracker.dagger.scope.AppScope
import com.shashavs.simpletracker.permission.AppPermission
import com.shashavs.simpletracker.preferences.AppPreference
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application) {

    @AppScope
    @Provides
    fun provideApplication() = application

//    @Singleton
//    @Provides
//    fun provideContext() = context

    @AppScope
    @Provides
    fun getSharedPreferences() = PreferenceManager.getDefaultSharedPreferences(application)

    @AppScope
    @Provides
    fun getAppPreference(preference: SharedPreferences) = AppPreference(preference)

    @AppScope
    @Provides
    fun getPermission() = AppPermission(application)

}