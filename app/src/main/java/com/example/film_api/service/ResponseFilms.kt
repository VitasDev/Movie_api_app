package com.example.film_api.service

import com.google.gson.annotations.SerializedName

data class ResponseFilms(
    @SerializedName("results") val films : List<Film>
)

data class Film (
    @SerializedName("original_title")
    var title: String,
    @SerializedName("overview")
    var description: String,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("vote_average")
    var rating: Float
)
