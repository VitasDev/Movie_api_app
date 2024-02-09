package com.example.film_api.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.film_api.model.Films

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFilms(films: List<Films>)

    @Query("SELECT * FROM film_table ORDER BY id ASC")
    fun readAllFilms(): LiveData<List<Films>>
}