package com.example.film_api.singleton

import android.app.Application
import com.example.film_api.Constants.BASE_URL
import com.example.film_api.database.FilmDao
import com.example.film_api.database.FilmDatabase
import com.example.film_api.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesApiService(): ApiService =
        Retrofit.Builder()
            .run {
                baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesFilmDao(application: Application): FilmDao =
        FilmDatabase.getDatabase(application).filmDao()
}