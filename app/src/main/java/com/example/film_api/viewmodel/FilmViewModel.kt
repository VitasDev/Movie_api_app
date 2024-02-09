package com.example.film_api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.film_api.data.FilmDatabase
import com.example.film_api.model.Films
import com.example.film_api.repository.FilmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmViewModel(application: Application): AndroidViewModel(application) {

    val readAllFilms: LiveData<List<Films>>
    private val repository: FilmRepository

    init {
        val filmDao = FilmDatabase.getDatabase(application).filmDao()
        repository = FilmRepository(filmDao)
        readAllFilms = repository.readAllFilms
    }
    
    fun addFilms(films: List<Films>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFilms(films)
        }
    }
}