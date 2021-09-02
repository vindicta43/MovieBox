package com.alperen.moviebox.ui.main.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alperen.moviebox.R
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.ui.main.homepage.MainPageRecyclerAdapter.*
import com.bumptech.glide.Glide

class MainPageRecyclerAdapter(private val list: ArrayList<ModelShow>) :
    RecyclerView.Adapter<ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivShowImg = view.findViewById<ImageView>(R.id.ivShowImg)
        val tvShowName = view.findViewById<TextView>(R.id.tvShowName)
        val tvGenres = view.findViewById<TextView>(R.id.tvGenres)
        val tvRating = view.findViewById<TextView>(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_page_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progress = CircularProgressDrawable(holder.itemView.context)
        progress.centerRadius = 16f
        progress.start()

        Glide
            .with(holder.ivShowImg.context)
            .load(list[position].image?.medium)
            .placeholder(progress)
            .into(holder.ivShowImg)

        holder.tvShowName.text = list[position].name
        holder.tvGenres.text = list[position].genres.toString()
        holder.tvRating.text = list[position].rating?.average.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}