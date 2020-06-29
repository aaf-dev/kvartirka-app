package com.example.kvartirkaapp.details.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.app.KvartirkaApplication.Companion.APPLICATION_LOGGING_TAG
import com.example.kvartirkaapp.details.ui.pagerAdapter.ViewPagerAdapter
import com.example.kvartirkaapp.extensions.ViewModelFactory
import com.example.kvartirkaapp.main.api.PhotoData
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.domain.Prices
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.tool_bar.view.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

class DetailsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private lateinit var viewModel: DetailsViewModel

    private var flatId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        (activity as AppCompatActivity).setSupportActionBar(view.toolbar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        flatId = arguments?.getInt(ARGUMENT_FLAT_ID) ?: 0

        setupViewModel()
        getFlatInformation()
    }

    private fun setupViewModel() {
        viewModel = ViewModelFactory(kodein).create(DetailsViewModel::class.java)
    }

    private fun getFlatInformation() {
        setupContentVisibility(true)

        viewModel
            .getFlatById(flatId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(
                {
                    setupFlatDetails(it)
                },
                {
                    Log.e(APPLICATION_LOGGING_TAG, "Error: ", it)
                }
            )
    }

    private fun setupFlatDetails(flat: FlatModel) {
        val title = "${flat.title}, ${flat.address}"
        setupContentVisibility(false, title)

        setupViewPager(flat.photos)
        setupFlatPrices(flat.prices)
        setupFlatTitle(flat.title)
        setupFlatAddress(flat.address)
        setupFlatDescription(flat.description)
    }

    private fun setupViewPager(images: List<PhotoData>) {
        viewPager.adapter = ViewPagerAdapter(images)
    }

    private fun setupFlatPrices(prices: Prices) {

        val labels = resources.getStringArray(R.array.prices)

        for (i in 0..1) {
            val row = TableRow(requireContext())
            row.apply {
                layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            }

            for (j in 0..2) {
                val textView = TextView(requireContext())
                textView.apply {
                    gravity = Gravity.CENTER
                }
                if (i == 0) {
                    textView.text = labels[j]
                    textView.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                } else if (i != 0) {
                    textView.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto)
                    when (j) {
                        0 -> textView.text = prices.pricePerDay
                        1 -> textView.text = prices.pricePerNight
                        2 -> textView.text = prices.pricePerHour
                    }
                }
                row.addView(textView, j)
            }
            pricesLayout.addView(row, i)
        }
    }

    private fun setupFlatTitle(title: String) {
        flatTitleTextView.text = title
    }

    private fun setupFlatAddress(address: String) {
        flatAddressTextView.text = address
    }

    private fun setupFlatDescription(description: String) {
        flatDescriptionTextView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
    }

    private fun setupContentVisibility(
        isLoading: Boolean,
        title: String = getString(R.string.app_name)
    ) {
        if (isLoading) {
            view?.toolbar?.title = getString(R.string.loading_title)
            progressLayout.visibility = View.VISIBLE
            detailsContent.visibility = View.GONE
        } else {
            view?.toolbar?.title = title
            view?.toolbar?.navigationIcon = activity?.getDrawable(R.drawable.ic_baseline_arrow_back_24)
            progressLayout.visibility = View.GONE
            detailsContent.visibility = View.VISIBLE

            setupNavigationClickListener()
        }
    }

    private fun setupNavigationClickListener() {
        view?.toolbar?.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }
    }

    companion object {
        const val ARGUMENT_FLAT_ID = "ARG_FLAT_ID"
    }
}