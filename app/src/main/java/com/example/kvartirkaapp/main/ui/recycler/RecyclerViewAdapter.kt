package com.example.kvartirkaapp.main.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.main.domain.FlatModel
import kotlinx.android.synthetic.main.recycler_item.view.*

class RecyclerViewAdapter(var items: List<FlatModel>) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )

    fun getFlatIdByPosition(position: Int): Int = items[position].id

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setImage(items[position].defaultPhoto.url)
        holder.setTitle(items[position].title)
        holder.setAddress(items[position].address)
        holder.setPrice(items[position].prices.pricePerDay)
    }
}

class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.image
    private val title = view.title
    private val address = view.address
    private val price = view.price

    fun setImage(imageUrl: String) {
        Glide
            .with(view)
            .load(imageUrl)
            .error(R.drawable.ic_baseline_error_24)
            .centerCrop()
            .into(image)
    }

    fun setTitle(flatTitle: String) {
        title.text = flatTitle
    }

    fun setAddress(flatAddress: String) {
        address.text = flatAddress
    }

    fun setPrice(flatPricePerDay: String) {
        price.text = flatPricePerDay
    }
}