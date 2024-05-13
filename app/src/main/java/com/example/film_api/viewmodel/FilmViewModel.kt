package com.example.film_api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.film_api.datastate.DataState
import com.example.film_api.data.Film
import com.example.film_api.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(private val repository: FilmRepository) : ViewModel() {

    private val _dataState = MutableStateFlow<DataState>(DataState.Empty)
    val dataState: StateFlow<DataState> = _dataState

    private val _findFilmState = MutableStateFlow<DataState>(DataState.Empty)
    val findFilmState: StateFlow<DataState> = _findFilmState

    init {
        readAllFilms()
    }

    fun getFilms(textSearch: String) =
        viewModelScope.launch {
            repository.fetchFilms(textSearch).onStart {
                _findFilmState.value = DataState.Loading
            }.catch {
                _findFilmState.value = DataState.Error
            }.collect {
                _findFilmState.value = DataState.Success(it.results)

                val resultFilms = it.results
                if (resultFilms.isNotEmpty())
                    addFilms(resultFilms)
                else
                    _findFilmState.value = DataState.EmptyData
            }
        }

    private fun readAllFilms() {
        viewModelScope.launch {
            _dataState.value = DataState.Loading
            repository.readAllFilms.collect {
                if (it.isNotEmpty()) {
                    _dataState.value = DataState.Success(it)
                } else {
                    _dataState.value = DataState.EmptyData
                }
            }
        }
    }

    private fun addFilms(films: List<Film>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFilms(films)
        }
    }
}