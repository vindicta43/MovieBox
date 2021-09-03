package com.alperen.moviebox.ui.main.homepage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.alperen.moviebox.R
import com.alperen.moviebox.models.show.ModelDetails
import com.alperen.moviebox.models.user.show.ModelShow
import com.alperen.moviebox.ui.main.homepage.HomePageRecyclerAdapter.*
import com.bumptech.glide.Glide

class HomePageRecyclerAdapter(private val list: ArrayList<ModelShow>) :
    RecyclerView.Adapter<ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivShowImg: ImageView = view.findViewById(R.id.ivShowImg)
        val tvShowName: TextView = view.findViewById(R.id.tvShowName)
        val tvGenres: TextView = view.findViewById(R.id.tvGenres)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val ibShare: ImageButton = view.findViewById(R.id.ibShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_page_recycler_item, parent, false)
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

        holder.ibShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${list[position].name}\n${list[position].officialSite}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            holder.itemView.context.startActivity(shareIntent)
        }

        holder.itemView.setOnClickListener {
            val action = HomePageFragmentDirections.actionMainPageFragmentToDetailsFragment(
                ModelDetails(
                    list[position].name,
                    list[position].summary,
                    list[position].rating?.average.toString(),
                    list[position].image?.original.toString(),
                    list[position].genres
                    )
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}