package com.example.film_api.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.film_api.model.Films

@Database(entities = [Films::class], version = 1, exportSchema = false)
abstract class FilmDatabase: RoomDatabase() {

    abstract fun filmDao(): FilmDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getDatabase(context: Context): FilmDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmDatabase::class.java,
                    "film_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}