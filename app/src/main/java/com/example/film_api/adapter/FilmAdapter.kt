package com.example.film_api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.film_api.Constants.IMAGE_URL
import com.example.film_api.R
import com.example.film_api.interfaces.OnItemClickListener
import com.example.film_api.model.Films

class FilmAdapter(private val mOnItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private var filmsList = emptyList<Films>()

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPoster = itemView.findViewById<ImageView>(R.id.img_poster)!!
        var txtTitle = itemView.findViewById<TextView>(R.id.txt_title)!!
        var txtDescription = itemView.findViewById<TextView>(R.id.txt_description)!!
        var containerFilm = itemView.findViewById<ConstraintLayout>(R.id.container_film)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_film, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentItem = filmsList[position]

        holder.txtTitle.text = currentItem.name
        holder.txtDescription.text = currentItem.description

        Glide.with(holder.itemView)
            .load(IMAGE_URL + currentItem.poster)
            .placeholder(R.drawable.img_empty_poster)
            .into(holder.imgPoster)

        holder.containerFilm.setOnClickListener {
            mOnItemClickListener.onItemClick(currentItem)
        }
    }

    fun setData(films: List<Films>) {
        this.filmsList = films.reversed()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filmsList.size
    }
}