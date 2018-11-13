package com.shashavs.simpletracker.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.shashavs.simpletracker.R
import javax.inject.Inject

class AppPermission @Inject constructor(val context : Context){

    val PERMISSION_REQUEST = 783
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkPermission(activity: AppCompatActivity): Boolean {
        if(!hasPermission()) {
            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermission(activity)) {
                showPermissionExplanation(activity)
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST)
            }
            return false
        } else {
            return true
        }
    }

    private fun shouldShowRequestPermission(activity: AppCompatActivity): Boolean {
        permissions.forEach {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                return true
            }
        }
        return false
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions.sumBy { permission: String ->
                ContextCompat.checkSelfPermission(context, permission) } == PackageManager.PERMISSION_GRANTED

        } else true
    }

    private fun showPermissionExplanation(activity: AppCompatActivity) = AlertDialog.Builder(activity)
        .setMessage(R.string.permission_message)
        .setPositiveButton(R.string.yes) { dialog, which ->
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST)
        }
        .setNegativeButton(R.string.no, null)
        .create()
        .show()
}