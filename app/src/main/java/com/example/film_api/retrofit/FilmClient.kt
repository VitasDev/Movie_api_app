package com.example.film_api.retrofit

import com.example.film_api.service.ApiService

object FilmClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
}