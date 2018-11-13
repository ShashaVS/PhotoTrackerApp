package com.shashavs.simpletracker.main

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.shashavs.simpletracker.R
import com.shashavs.simpletracker.dagger.component.ActivitySubComponent
import com.shashavs.simpletracker.dagger.module.CameraModule
import com.shashavs.simpletracker.dagger.module.ViewModelModule
import com.shashavs.simpletracker.fragments.TrackerMapFragment
import com.shashavs.simpletracker.main.viewmodel.AppViewModel
import com.shashavs.simpletracker.main.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.main.container_layout.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainInterface {

    private lateinit var viewModel: AppViewModel
    @Inject lateinit var viewModelFactory: AppViewModelFactory

    private val activityComponent by lazy { (application as App).component.subComponent(ViewModelModule(), CameraModule()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AppViewModel::class.java)

        if(viewModel.isEmptyTag()) {
            viewModel.addTag(getString(R.string.tracker_map))
            supportFragmentManager.beginTransaction()
                .add(R.id.container, TrackerMapFragment(), viewModel.getLastTag())
                .commitNow()
            toolbar.title = viewModel.getLastTag()
        } else {
            updateActionBar()
        }
    }

    override fun activityComponent(): ActivitySubComponent? = activityComponent

    override fun addFragment(tag: String, fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentByTag(viewModel.getLastTag())

        if (currentFragment != null) {
            supportFragmentManager.beginTransaction()
                .hide(currentFragment)
                .add(R.id.container, fragment, tag)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()

            viewModel.addTag(tag)
            toolbar.title = tag
            updateActionBar()
        }
    }

    private fun updateActionBar() {
        if(viewModel.tagsCount() == 1) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false);
            supportActionBar?.setDisplayShowHomeEnabled(false);
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed();
        return true;
    }

    override fun onBackPressed() {
        if(viewModel.tagsCount() > 1) {
            viewModel.removeLastTag()
            toolbar.title = viewModel.getLastTag()
            updateActionBar()
        }
        super.onBackPressed()
    }

}
