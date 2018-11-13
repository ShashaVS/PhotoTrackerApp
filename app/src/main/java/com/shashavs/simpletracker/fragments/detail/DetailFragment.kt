package com.shashavs.simpletracker.fragments.detail

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shashavs.simpletracker.R

import com.shashavs.simpletracker.main.MainInterface
import com.shashavs.simpletracker.main.viewmodel.AppViewModel
import com.shashavs.simpletracker.main.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.main.fragment_item_list.*
import javax.inject.Inject

class DetailFragment : Fragment() {

    private var mainInterface: MainInterface? = null
    private var id: String? = null
    private lateinit var viewModel: AppViewModel
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainInterface?.activityComponent()?.inject(this)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(AppViewModel::class.java)
        id = arguments?.getString(ARG_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title.text = "Id: $id"
        val urls = viewModel.getImageUrlById(id)

        list.layoutManager = GridLayoutManager(context, 2)
        list.adapter = DetailAdapter(requireContext(), urls)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }

    companion object {

        const val ARG_ID = "id"

        @JvmStatic
        fun newInstance(id: String) = DetailFragment().apply { arguments = Bundle().apply { putString(ARG_ID, id) }}
    }
}
