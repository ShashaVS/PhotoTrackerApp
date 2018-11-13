package com.shashavs.simpletracker.main.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.net.Uri
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.storage.UploadTask
import com.shashavs.simpletracker.R
import com.shashavs.simpletracker.firebase.AppFirebaseStorage

import com.shashavs.simpletracker.firebase.AppRealtimeReference
import com.shashavs.simpletracker.location.TrackerLocation
import com.shashavs.simpletracker.preferences.AppPreference
import java.lang.Exception
import java.net.URLDecoder
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.collections.HashMap

class AppViewModel @Inject constructor(val app: Application,
                                       val preferences: AppPreference,
                                       val trackerLocation: TrackerLocation,
                                       val realtimeDatabase: AppRealtimeReference,
                                       val firebaseStorage: AppFirebaseStorage):
    AndroidViewModel(app) {

    private val TAG = "AppViewModel"

    private var tagList: MutableList<String>
    private var locationCallback: LocationCallback? = null
    private var locationListener: ChildEventListener? = null
    private var imagesListener: ChildEventListener? = null

    private val id: String
    private val refLocation = "locations"
    private val refImages = "images"

    private val markersMap: MutableMap<String, Marker>
    private val imagesMap: MutableMap<String, HashMap<*, *>>

    var googleMap: GoogleMap? = null
    set(value) {
        field = value
        realtimeDatabase.addListener(refLocation, locationListener)
    }

    init {
        tagList = mutableListOf()
        markersMap = mutableMapOf()
        imagesMap = mutableMapOf()

        id = preferences.getId()
        realtimeDatabase.addReference(refLocation, "$refLocation/$id")
        realtimeDatabase.addReference(refImages, "$refImages/$id")

        locationListener = object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                try {
                    val key = p0.key
                    if(key != null && !key.equals(id)) {
                        val value = p0.value as HashMap<*, *>
                        val latitude = value.get("latitude") as Double
                        val longitude = value.get("longitude") as Double

                        val coordinates = LatLng(latitude, longitude)
                        addMarker(coordinates, key)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onChildAdded: ", e)
                }
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                try {
                    val key = p0.key
                    if(key != null && !key.equals(id)) {
                        val value = p0.value as HashMap<*, *>
                        val latitude = value.get("latitude") as Double
                        val longitude = value.get("longitude") as Double
                        updateMarkerLocation(LatLng(latitude, longitude), key)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onChildChanged: ", e)
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                val key = p0.key
                if(key != null) {
                    markersMap.get(key)?.remove()
                    markersMap.remove(key)
                    updateMarkers();
                }
            }
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
        }

        imagesListener = object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) { updateImagesMap(p0) }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) { updateImagesMap(p0) }

            override fun onChildRemoved(p0: DataSnapshot) { }

            override fun onCancelled(p0: DatabaseError) { }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
        }

        realtimeDatabase.addListener(refImages, imagesListener)

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                val location = locationResult.lastLocation
                if(!markersMap.containsKey(id)) {
                    addMarker(LatLng(location.latitude, location.longitude), id)
                } else {
                    updateMarkerLocation(LatLng(location.latitude, location.longitude), id)
                }
                realtimeDatabase.setValue("locations", location)
            }
        }

    }

    private fun addMarker(coordinates: LatLng, key: String) {
        val marker = googleMap?.addMarker(MarkerOptions().position(coordinates).title("id: $key"))
        marker?.tag = key
        marker?.snippet = app.getString(R.string.marker_snippet)
        marker?.setIcon(BitmapDescriptorFactory.defaultMarker(Math.random().times(360).toFloat()))

        if(marker != null) {
            markersMap.put(key, marker)
            updateMarkers();
        }
    }

    private fun updateMarkerLocation(coordinates: LatLng, key: String) {
        markersMap.get(key)?.position = coordinates
        updateMarkers();
    }

    private fun updateMarkers() {
        if(markersMap.size > 1) {
            val latLngBuilder = LatLngBounds.Builder()
            val iterator = markersMap.iterator()
            while (iterator.hasNext()) {
                latLngBuilder.include(iterator.next().value.position)
            }
            val latLngBounds = latLngBuilder.build()

            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100))
        } else {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(markersMap.values.first().position, 15f))
        }
    }

    private fun updateImagesMap(snapshot: DataSnapshot) {
        try {
            val key = snapshot.key
            if(key != null) {
                val value = snapshot.value as HashMap<*, *>
                imagesMap.put(key, value)
            }
        } catch (e: Exception) {
            Log.e(TAG, "onChildAdded: ", e)
        }
    }

    fun startLocation() {
        trackerLocation.startLocationUpdates(locationCallback)
    }

    fun stopLocation() {
        trackerLocation.stopLocationUpdates()
    }

    fun uploadPhoto(path: String?) {
        firebaseStorage.uploadFile(path, refImages.plus("/").plus(id).plus("/"))
            ?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation firebaseStorage.reference?.downloadUrl
                })
            ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val name = firebaseStorage.reference?.name?.substringBefore('.')
                val downloadUri = task.result.toString()
                val value = URLEncoder.encode(downloadUri, "utf-8")
                realtimeDatabase.setValue(refImages, name, value)
            }
        }
    }

    fun getImageUrlById(key: String?): List<Any>? {
        if(imagesMap.containsKey(key)) {
            val urlMap = imagesMap.get(key)
            return urlMap?.values?.toList()?.map {
                URLDecoder.decode(it.toString(), "utf-8")
            }
        }
        return null
    }

    fun isEmptyTag() = tagList.isEmpty()

    fun tagsCount() = tagList.size

    fun addTag(tag: String) = tagList.add(tag)

    fun removeLastTag() = tagList.removeAt(tagList.lastIndex)

    fun getLastTag() = tagList.last()

    override fun onCleared() {
        stopLocation()

        realtimeDatabase.removeListener(refLocation, locationListener)
        realtimeDatabase.removeListener(refImages, imagesListener)
        realtimeDatabase.removeValue(refLocation)

        locationListener = null
        imagesListener = null
        locationCallback = null
        googleMap = null

        super.onCleared()
    }

}