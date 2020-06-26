package com.example.kvartirkaapp.details.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TableRow.LayoutParams
import android.widget.TextView
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.details.ui.pagerAdapter.ViewPagerAdapter
import com.example.kvartirkaapp.extensions.ViewModelFactory
import com.example.kvartirkaapp.main.api.PhotoData
import com.example.kvartirkaapp.main.domain.FlatModel
import com.example.kvartirkaapp.main.domain.Prices
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_details.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class DetailsActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by kodein()

    private var id: Int = 0

    private lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setLoadingVisibility(true)

        id = intent.getIntExtra("EXTRA_ID", 0)

        setupViewModel()
        getFlatInformation()
    }

    private fun setupViewModel() {
        viewModel = ViewModelFactory(kodein).create(DetailsViewModel::class.java)
    }

    private fun getFlatInformation() {
        viewModel
            .getFlatById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(lifecycle))
            .subscribe(
                {
                    setupFlatDetails(it)
                },
                {
                    Log.e("KVAPP", "Error: ", it)
                }
            )
    }

    private fun setupFlatDetails(flat: FlatModel) {
        setLoadingVisibility(false, "${flat.title}, ${flat.address}")
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
            val row = TableRow(this)
            row.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                gravity = Gravity.CENTER
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                dividerDrawable = resources.getDrawable(R.color.colorLightGrey, null)
            }

            for (j in 0..2) {
                val textView = TextView(this)
                textView.apply {
                    gravity = Gravity.CENTER
                }
                if (i == 0) {
                    textView.text = labels[j]
                } else if (i != 0 && j == 0) {
                    textView.text = prices.pricePerDay
                } else if (i != 0 && j == 1) {
                    textView.text = prices.pricePerNight
                } else if (i != 0 && j == 2) {
                    textView.text = prices.pricePerHour
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
        flatDescriptionTextView.text = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
    }

    private fun setLoadingVisibility(isLoadingInProgress: Boolean, activityTitle: String = "") {
        if (isLoadingInProgress) {
            title = "Загрузка..."
            detailsProgressBar.visibility = View.VISIBLE
            descriptionTitle.visibility = View.GONE
            divider.visibility = View.GONE
        } else {
            title = activityTitle
            detailsProgressBar.visibility = View.GONE
            descriptionTitle.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
        }
    }
}