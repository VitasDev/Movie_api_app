package com.example.film_api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_api.ReadFilmState
import com.example.film_api.data.FilmDao
import com.example.film_api.data.FilmDatabase
import com.example.film_api.model.Film
import com.example.film_api.repository.FilmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmViewModel(application: Application): AndroidViewModel(application) {

    private val repository: FilmRepository
    private val _readFilmState = MutableStateFlow<ReadFilmState>(ReadFilmState.Empty)
    val readFilmState: StateFlow<ReadFilmState> = _readFilmState

    init {
        val filmDao: FilmDao by lazy { FilmDatabase.getDatabase(application).filmDao() }
        repository = FilmRepository(filmDao)
        readAllFilms()
    }

    private fun readAllFilms() {
        viewModelScope.launch {
            _readFilmState.value = ReadFilmState.Loading
            repository.readAllFilms.collect {
                if (it.isNotEmpty()) {
                    _readFilmState.value = ReadFilmState.Success(it)
                } else {
                    _readFilmState.value = ReadFilmState.Error("List is Empty!")
                }
            }
        }
    }

    fun addFilms(films: List<Film>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFilms(films)
        }
    }
}