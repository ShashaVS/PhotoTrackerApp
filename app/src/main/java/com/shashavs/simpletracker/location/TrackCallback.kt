package com.shashavs.simpletracker.location

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.lang.ref.WeakReference

class TrackCallback(locationCallback: LocationCallback): LocationCallback() {

    private val weakLocationCallback = WeakReference<LocationCallback>(locationCallback)

    override fun onLocationResult(locationResult: LocationResult?) {
        locationResult ?: return
        super.onLocationResult(locationResult)
        weakLocationCallback.get()?.onLocationResult(locationResult)
    }

    fun clear() {
        weakLocationCallback.clear()
    }

}