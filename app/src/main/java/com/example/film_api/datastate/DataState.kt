package com.example.film_api.datastate

import com.example.film_api.data.Film

sealed class DataState {
    data object EmptyData : DataState()
    data class Success(val data: List<Film>) : DataState()
    data object Error : DataState()
    data object Loading : DataState()
    data object Empty : DataState()
}