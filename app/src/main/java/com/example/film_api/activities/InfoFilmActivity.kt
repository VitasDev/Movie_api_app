package com.example.film_api.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.film_api.Constants
import com.example.film_api.Constants.EXTRA_OBJ
import com.example.film_api.R
import com.example.film_api.databinding.ActivityInfoFilmBinding
import com.example.film_api.model.Films

class InfoFilmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoFilmBinding
    private lateinit var film: Films

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        film = intent.getSerializableExtra(EXTRA_OBJ) as Films

        binding.txtTitle.text = film.name
        binding.txtDescription.text = film.description

        Glide.with(this)
            .load(Constants.IMAGE_URL + film.poster)
            .placeholder(R.drawable.img_empty_poster)
            .into(binding.imgPoster)

        binding.txtRating.text = getString(R.string.rating, film.rating.toString())

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}