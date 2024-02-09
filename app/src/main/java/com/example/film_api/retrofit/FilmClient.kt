package com.example.film_api.retrofit

import com.example.film_api.service.FilmsService

object FilmClient {
    val apiService: FilmsService by lazy {
        RetrofitClient.retrofit.create(FilmsService::class.java)
    }
}