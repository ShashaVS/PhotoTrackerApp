package com.shashavs.simpletracker.firebase

import android.support.annotation.NonNull
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class AppRealtimeReference @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {

    private var referenceMap: MutableMap<String, DatabaseReference> = mutableMapOf()

    fun addReference(key: String, path: String) = referenceMap.put(key, firebaseDatabase.getReference(path))

    fun addListener(key: String, @NonNull listener: ChildEventListener?) {
        listener ?: return
        referenceMap.get(key)?.parent?.addChildEventListener(listener)
    }

    fun setValue(key: String, value: Any) {
        referenceMap.get(key)?.setValue(value)
    }

    fun setValue(key: String, name: String?, value: Any?) {
        name ?: return
        value ?: return
        referenceMap.get(key)?.child(name)?.setValue(value)
    }

    fun removeValue(key: String) {
        referenceMap.get(key)?.removeValue()
    }

    fun removeListener(key: String, @NonNull listener: ChildEventListener?) {
        listener ?: return
        referenceMap.get(key)?.parent?.removeEventListener(listener)
    }

}