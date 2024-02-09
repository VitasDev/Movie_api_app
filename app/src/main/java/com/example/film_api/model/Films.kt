package com.example.film_api.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "film_table")
data class Films (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = "",
    val description: String? = "",
    val poster: String? = "",
    val rating: Float? = 0f
) : Parcelable, Serializable