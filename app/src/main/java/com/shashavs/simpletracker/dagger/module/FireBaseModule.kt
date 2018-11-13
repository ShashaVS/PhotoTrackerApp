package com.shashavs.simpletracker.dagger.module

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.shashavs.simpletracker.dagger.scope.AppScope
import com.shashavs.simpletracker.firebase.AppRealtimeReference
import dagger.Module
import dagger.Provides

@Module
class FireBaseModule() {

    @AppScope
    @Provides
    fun getFirebaseDatabase(): FirebaseDatabase {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)    //  Enabling Offline Capabilities
        return firebaseDatabase
    }

    @AppScope
    @Provides
    fun getAppRealtimeReference(firebaseDatabase: FirebaseDatabase) =
        AppRealtimeReference(firebaseDatabase)

    @AppScope
    @Provides
    fun getFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

}