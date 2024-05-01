package com.example.film_api.repository

import com.example.film_api.data.FilmDao
import com.example.film_api.model.Film
import kotlinx.coroutines.flow.Flow

class FilmRepository(private val filmDao: FilmDao) {

    val readAllFilms: Flow<List<Film>> = filmDao.readAllFilms()

    suspend fun addFilms(films: List<Film>) {
        filmDao.addFilms(films)
    }
}