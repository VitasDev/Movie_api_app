package com.example.film_api

import com.example.film_api.model.Film

sealed class ReadFilmState {
    data class Success(val data: List<Film>) : ReadFilmState()
    data class Error(val message: String) : ReadFilmState()
    data object Loading : ReadFilmState()
    data object Empty : ReadFilmState()
}