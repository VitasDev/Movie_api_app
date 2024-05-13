package com.example.film_api.database

import androidx.room.*
import com.example.film_api.data.Film
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFilms(films: List<Film>)

    @Query("SELECT * FROM film_table ORDER BY id ASC")
    fun readAllFilms(): Flow<List<Film>>
}