package com.example.film_api.service

import com.example.film_api.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/movie")
    fun getFilms(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1
    ): Call<ResponseFilm>
}