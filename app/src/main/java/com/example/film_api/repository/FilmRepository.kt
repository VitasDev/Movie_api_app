package com.example.film_api.repository

import androidx.lifecycle.LiveData
import com.example.film_api.data.FilmDao
import com.example.film_api.model.Films

class FilmRepository(private val filmDao: FilmDao) {

    val readAllFilms: LiveData<List<Films>> = filmDao.readAllFilms()

    suspend fun addFilms(films: List<Films>) {
        filmDao.addFilms(films)
    }
}