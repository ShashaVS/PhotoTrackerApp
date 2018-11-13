package com.shashavs.simpletracker.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import javax.inject.Inject

class AppFirebaseStorage @Inject constructor(private val storage: FirebaseStorage) {

    private val storageRef = storage.reference
    var reference: StorageReference? = null

    fun uploadFile(filePpath: String?, referencePath: String): UploadTask? {
        filePpath ?: return null
        val file = Uri.fromFile(File(filePpath))
        reference = storageRef.child(referencePath.plus(file.lastPathSegment))
        return reference?.putFile(file)
    }
}