package com.example.kvartirkaapp.details.ui.pagerAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kvartirkaapp.R
import com.example.kvartirkaapp.main.api.PhotoData
import kotlinx.android.synthetic.main.pager_item.view.*

class ViewPagerAdapter(var images: List<PhotoData>) : RecyclerView.Adapter<ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder =
        ViewPagerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pager_item, parent, false)
        )

    override fun getItemCount(): Int = images.count()

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.setImage(images[position].url)
    }
}

class ViewPagerHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.pagerImage

    fun setImage(url: String) {
        Glide
            .with(view)
            .load(url)
            .centerCrop()
            .into(image)
    }
}