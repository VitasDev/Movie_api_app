package com.example.film_api.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.film_api.Constants
import com.example.film_api.Constants.IMAGE_URL
import com.example.film_api.R
import com.example.film_api.activities.InfoFilmActivity
import com.example.film_api.databinding.ItemFilmBinding
import com.example.film_api.model.Film

class FilmAdapter: RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    private var filmsList = emptyList<Film>()

    class FilmViewHolder(val itemBinding: ItemFilmBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val itemBinding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val currentItem = filmsList[position]

        holder.itemBinding.txtTitle.text = currentItem.name
        holder.itemBinding.txtDescription.text = currentItem.description

        Glide.with(holder.itemView)
            .load(IMAGE_URL + currentItem.poster)
            .placeholder(R.drawable.img_empty_poster)
            .into(holder.itemBinding.imgPoster)

        holder.itemBinding.containerFilm.setOnClickListener {
            val intent = Intent(it.context, InfoFilmActivity::class.java)
            intent.putExtra(Constants.EXTRA_OBJ, currentItem ?: "")
            it.context.startActivity(intent)
        }
    }

    fun setData(films: List<Film>) {
        this.filmsList = films.reversed()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filmsList.size
    }
}