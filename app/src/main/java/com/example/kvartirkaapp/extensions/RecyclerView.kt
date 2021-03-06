package com.example.kvartirkaapp.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.setOnItemClickListener(crossinline listener: (position: Int) -> Unit) {
    addOnItemTouchListener(
        RecyclerItemClickListener(this,
            object :
                RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    listener(position)
                }
            })
    )
}