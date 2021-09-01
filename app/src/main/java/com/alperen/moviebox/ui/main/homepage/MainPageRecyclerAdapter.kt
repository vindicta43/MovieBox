package com.alperen.moviebox.ui.main.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alperen.moviebox.R
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.ui.main.homepage.MainPageRecyclerAdapter.*

class MainPageRecyclerAdapter(private val list: ArrayList<ModelShow>) :
    RecyclerView.Adapter<ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // TODO: recyclerview item yapılacak
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return list.size
    }
}