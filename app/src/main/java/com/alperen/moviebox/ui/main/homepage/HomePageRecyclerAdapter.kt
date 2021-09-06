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
import com.alperen.moviebox.network.FirebaseUserUtils
import com.alperen.moviebox.ui.main.homepage.HomePageRecyclerAdapter.*
import com.alperen.moviebox.utils.AlertBuilder
import com.alperen.moviebox.utils.Constants
import com.alperen.moviebox.utils.ToastBuilder
import com.bumptech.glide.Glide

class HomePageRecyclerAdapter(
    private val showsList: ArrayList<ModelShow>,
) :
    RecyclerView.Adapter<ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivShowImg: ImageView = view.findViewById(R.id.ivShowImg)
        val tvShowName: TextView = view.findViewById(R.id.tvShowName)
        val tvGenres: TextView = view.findViewById(R.id.tvGenres)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val ibShare: ImageButton = view.findViewById(R.id.ibShare)
        val ibFavorite: ImageButton = view.findViewById(R.id.ibFavorite)
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
            .load(showsList[position].image?.medium)
            .placeholder(progress)
            .into(holder.ivShowImg)
        holder.tvShowName.text = showsList[position].name
        holder.tvGenres.text = showsList[position].genres.toString()
        holder.tvRating.text = showsList[position].rating?.average.toString()

        val showID = showsList[position].id.toString()
        FirebaseUserUtils.getUserFavorites().observeForever { modelUser ->
            if (modelUser?.contains(showID) == true) {
                holder.ibFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                holder.ibFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        holder.ibShare.setOnClickListener {
            share(position, holder)
        }

        holder.itemView.setOnClickListener {
            val action = HomePageFragmentDirections.actionMainPageFragmentToDetailsFragment(
                ModelDetails(
                    showsList[position].id.toString(),
                    showsList[position].name,
                    showsList[position].summary,
                    showsList[position].rating?.average.toString(),
                    showsList[position].image?.original.toString(),
                    showsList[position].genres
                )
            )
            holder.itemView.findNavController().navigate(action)
        }

        holder.ibFavorite.setOnClickListener {
            val err = holder.itemView.resources.getString(R.string.alert_dialog_error)
            val msgAdd = holder.itemView.resources.getString(R.string.alert_dialog_favorite_add_success)
            val msgRemove = holder.itemView.resources.getString(R.string.alert_dialog_favorite_remove_success)

            FirebaseUserUtils.getUserFavorites().observeForever { favorites ->
                // if show already in favorites
                if (favorites?.contains(showID)!!) {
                    // remove from favorite
                    FirebaseUserUtils.removeFromFavorite(showID).observeForever { observer ->
                        // success remove
                        if (observer.equals(Constants.SUCCESS)) {
                            holder.ibFavorite.setImageResource(R.drawable.ic_favorite_border)
                            ToastBuilder(holder.itemView.context).build(msgRemove)
                        }
                        // error
                        else {
                            AlertBuilder(holder.itemView.context).build(err, observer)
                        }
                    }
                }
                // add show into favorites
                else {
                    FirebaseUserUtils.makeFavorite(showID).observeForever { observer ->
                        // success add
                        if (observer.equals(Constants.SUCCESS)) {
                            holder.ibFavorite.setImageResource(R.drawable.ic_favorite)
                            ToastBuilder(holder.itemView.context).build(msgAdd)
                        }
                        // error
                        else {
                            AlertBuilder(holder.itemView.context).build(err, observer)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return showsList.size
    }

    private fun share(position: Int, holder: ViewHolder) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "${showsList[position].name}\n${showsList[position].officialSite}"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        holder.itemView.context.startActivity(shareIntent)
    }
}