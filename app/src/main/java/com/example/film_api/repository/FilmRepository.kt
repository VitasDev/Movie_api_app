package com.example.film_api.repository

import com.example.film_api.database.FilmDao
import com.example.film_api.data.Film
import com.example.film_api.service.ApiService
import com.example.film_api.data.ResponseFilm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilmRepository @Inject constructor(private val apiService: ApiService, private val filmDao: FilmDao) {

    val readAllFilms: Flow<List<Film>> = filmDao.readAllFilms()

    suspend fun addFilms(films: List<Film>) {
        filmDao.addFilms(films)
    }

    fun fetchFilms(textSearch: String): Flow<ResponseFilm> = flow {
        emit(apiService.getFilms(textSearch))
    }.flowOn(Dispatchers.IO)
}