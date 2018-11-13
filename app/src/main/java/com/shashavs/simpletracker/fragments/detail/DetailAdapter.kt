package com.shashavs.simpletracker.fragments.detail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.shashavs.simpletracker.R

import kotlinx.android.synthetic.main.fragment_item.view.*

class DetailAdapter(val context: Context, private val values: List<Any>?) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private val glideOoptions: RequestOptions

    init {
        glideOoptions = RequestOptions()
            .centerInside()
            .placeholder(R.drawable.ic_launcher_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(values?.get(position))
            .apply(glideOoptions)
            .into(holder.image)
    }

    override fun getItemCount(): Int = values?.size ?: 0

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image = view.image
    }
}
