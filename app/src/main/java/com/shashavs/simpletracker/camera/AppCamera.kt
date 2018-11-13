package com.shashavs.simpletracker.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AppCamera @Inject constructor(){

    val REQUEST_CAMERA = 784
    var currentPhotoPath: String? = null

    fun dispatchTakePictureIntent(fragment: Fragment) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(fragment.requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(fragment.requireContext())
                } catch (ex: IOException) {
                    Log.e("TEST", "Error occurred while creating the File ", ex)
                    null
                }
                photoFile?.also {
                    val photoURI: Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            photoURI = FileProvider.getUriForFile(fragment.requireContext(),"com.shashavs.simpletracker.fileprovider", it)
                    } else {
                            photoURI = Uri.fromFile(it);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, REQUEST_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

}