package com.shashavs.simpletracker.dagger.module

import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.shashavs.simpletracker.dagger.scope.AppScope
import com.shashavs.simpletracker.location.TrackerLocation
import dagger.Module
import dagger.Provides

@Module
class LocationModule() {

    @AppScope
    @Provides
    fun getFusedLocationProviderClient(application: Application): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)

    @AppScope
    @Provides
    fun getLocationManager(application: Application): LocationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @AppScope
    @Provides
    fun getLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = 5000L
            fastestInterval = 5000L
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 0F
        }
    }

    @AppScope
    @Provides
    fun getLocationObject(locationManager: LocationManager,
                          fusedLocationClient: FusedLocationProviderClient,
                          locationRequest: LocationRequest): TrackerLocation {

        return TrackerLocation(locationManager, fusedLocationClient, locationRequest)
    }

}