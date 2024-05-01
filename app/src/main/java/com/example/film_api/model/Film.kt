package com.example.film_api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "film_table")
data class Film (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = "",
    val description: String? = "",
    val poster: String? = "",
    val rating: Float? = 0f
) : Serializable