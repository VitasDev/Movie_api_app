package com.example.film_api.service

import com.example.film_api.Constants
import com.example.film_api.data.ResponseFilm
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/movie")
    suspend fun getFilms(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): ResponseFilm
}