package com.example.film_api.data

import com.google.gson.annotations.SerializedName

data class ResponseFilm(
    @SerializedName("results") val results: List<Film>
)
