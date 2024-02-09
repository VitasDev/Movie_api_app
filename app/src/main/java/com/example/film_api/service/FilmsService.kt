package com.example.film_api.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsService {
    @GET("search/movie")
    fun getFilms(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<ResponseFilms?>
}