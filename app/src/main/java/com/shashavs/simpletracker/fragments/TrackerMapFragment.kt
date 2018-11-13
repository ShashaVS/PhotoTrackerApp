package com.shashavs.simpletracker.fragments

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.shashavs.simpletracker.R
import com.shashavs.simpletracker.camera.AppCamera
import com.shashavs.simpletracker.fragments.detail.DetailFragment

import com.shashavs.simpletracker.main.MainInterface
import com.shashavs.simpletracker.main.viewmodel.AppViewModel
import com.shashavs.simpletracker.main.viewmodel.AppViewModelFactory
import com.shashavs.simpletracker.permission.AppPermission

import kotlinx.android.synthetic.main.fragment_maps.*
import javax.inject.Inject

class TrackerMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: AppViewModel
    @Inject lateinit var viewModelFactory: AppViewModelFactory
    @Inject lateinit var permission: AppPermission
    @Inject lateinit var appCamera: AppCamera

    private var mainInterface: MainInterface? = null
    private var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainInterface?.activityComponent()?.inject(this)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.googleMap == null) {
            mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
            mapFragment?.getMapAsync(this)

            if(permission.checkPermission(requireActivity() as AppCompatActivity)) {
                viewModel.startLocation()
            }
        }

        fab.setOnClickListener {
            appCamera.dispatchTakePictureIntent(this)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainInterface = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if(viewModel.googleMap == null) {

            googleMap.setMaxZoomPreference(15f)
            googleMap.setMapStyle(MapStyleOptions(resources.getString(R.string.map_style_json)))

            googleMap.setOnInfoWindowClickListener { marker ->
                mainInterface?.addFragment(getString(R.string.detail), DetailFragment.newInstance(marker.tag.toString()))
            }
            viewModel.googleMap = googleMap
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == permission.PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults.all{ it == PackageManager.PERMISSION_GRANTED }) {
                viewModel.startLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == appCamera.REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            viewModel.uploadPhoto(appCamera.currentPhotoPath)
        }
    }

}
