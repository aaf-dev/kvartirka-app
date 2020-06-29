package com.example.kvartirkaapp.main.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.app.KvartirkaApplication.Companion.APPLICATION_LOGGING_TAG
import com.example.kvartirkaapp.app.MainActivity
import com.example.kvartirkaapp.details.ui.DetailsFragment
import com.example.kvartirkaapp.details.ui.DetailsFragment.Companion.ARGUMENT_FLAT_ID
import com.example.kvartirkaapp.extensions.ViewModelFactory
import com.example.kvartirkaapp.extensions.setOnItemClickListener
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.ui.recycler.RecyclerViewAdapter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import kotlinx.android.synthetic.main.tool_bar.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class MainFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        (activity as AppCompatActivity).setSupportActionBar(view.toolbar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setSupportActionBar(toolbar)

        setupViewModel()
        getUserLocation()
    }

    private fun setupViewModel() {
        viewModel = ViewModelFactory(kodein).create(MainViewModel::class.java)
    }

    private fun getUserLocation() {
        setupContentVisibility(true)

        viewModel
            .getUserLocation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    getFlats(it.latitude, it.longitude)
                },
                {
                    Log.e(APPLICATION_LOGGING_TAG, "Error: ", it)
                }
            )
    }

    private fun getFlats(latitude: Double, longitude: Double) {
        viewModel
            .getFlats(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle))
            .subscribe(
                {
                    setupRecyclerView(it)
                },
                {
                    Log.e(APPLICATION_LOGGING_TAG, "Error: ", it)
                }
            )
    }

    private fun setupRecyclerView(flats: List<FlatModel>) {
        setupContentVisibility(false)

        val adapter = RecyclerViewAdapter(flats)
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setOnItemClickListener {
            val id = adapter.getFlatIdByPosition(it)
            showDetailsFragment(id)
        }
    }

    private fun setupContentVisibility(isLoading: Boolean, title: String = getString(R.string.app_name)) {
        if (isLoading) {
            view?.toolbar?.title = getString(R.string.loading_title)
            progressLayout.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            view?.toolbar?.title = title
            progressLayout.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }
    }

    private fun showDetailsFragment(flatId: Int) {
        val detailsFragment = DetailsFragment().apply {
            val bundle = Bundle()
            bundle.putInt(ARGUMENT_FLAT_ID, flatId)
            arguments = bundle
        }
        fragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(R.animator.animation_enter_from_left, R.animator.animation_exit_to_right, R.animator.animation_enter_from_right, R.animator.animation_exit_to_left)
            ?.replace(R.id.fragmentContainer, detailsFragment)
            ?.addToBackStack(MainFragment().javaClass.name)
            ?.commit()
    }
}