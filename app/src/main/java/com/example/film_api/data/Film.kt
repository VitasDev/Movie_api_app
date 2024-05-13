package com.example.film_api.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "film_table")
data class Film(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int,
    @SerializedName("original_title")
    var title: String? = "",
    @SerializedName("overview")
    var description: String? = "",
    @SerializedName("poster_path")
    var posterPath: String? = "",
    @SerializedName("vote_average")
    var rating: Float? = 0f
): Serializable