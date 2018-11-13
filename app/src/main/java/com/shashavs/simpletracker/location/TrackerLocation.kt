package com.shashavs.simpletracker.location

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import java.lang.Exception
import javax.inject.Inject

class TrackerLocation @Inject constructor(val locationManager: LocationManager,
                                          val fusedLocationClient: FusedLocationProviderClient,
                                          val locationRequest: LocationRequest) {

    private var trackCallback: TrackCallback? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(locationCallback: LocationCallback?) {
        if(isEnableGPS() && locationCallback != null) {
            trackCallback = TrackCallback(locationCallback)
            fusedLocationClient.requestLocationUpdates(locationRequest, trackCallback, Looper.myLooper())
        }
    }

    fun stopLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(trackCallback)
            trackCallback?.clear()
            trackCallback = null
        } catch (e: Exception) {
            Log.d("TEST", "stopLocationUpdates Exception: ", e)
        }
    }

    fun isEnableGPS() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

}