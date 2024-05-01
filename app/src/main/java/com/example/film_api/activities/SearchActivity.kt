package com.example.film_api.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.film_api.Constants.API_KEY
import com.example.film_api.QueryTextListener
import com.example.film_api.R
import com.example.film_api.ReadFilmState
import com.example.film_api.adapter.FilmAdapter
import com.example.film_api.databinding.ActivitySearchBinding
import com.example.film_api.model.Film
import com.example.film_api.retrofit.FilmClient
import com.example.film_api.viewmodel.FilmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val scope = CoroutineScope(Dispatchers.Main)
    private val filmViewModel: FilmViewModel by viewModels()
    private lateinit var connectivityService: ConnectivityManager
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val adapterFilms = FilmAdapter()

        binding.rvFilms.adapter = adapterFilms
        binding.rvFilms.layoutManager = GridLayoutManager(this, 2)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filmViewModel.readFilmState.collect {
                    when(it) {
                        is ReadFilmState.Success -> {
                            binding.progressCircular.visibility = View.GONE
                            binding.rvFilms.visibility = View.VISIBLE
                            adapterFilms.setData(it.data)
                        }
                        is ReadFilmState.Error -> {
                            binding.progressCircular.visibility = View.GONE
                            Toast.makeText(
                                binding.root.context,
                                it.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is ReadFilmState.Loading -> {
                            binding.rvFilms.visibility = View.GONE
                            binding.progressCircular.visibility = View.VISIBLE
                        }
                        else -> Unit
                    }
                }
            }
        }

        binding.editTextEnterFilmName.setOnQueryTextListener(
            QueryTextListener(
                this,
                this@SearchActivity.lifecycle
            ) { newQueryText ->
                searchByFilmName(newQueryText!!)
            }
        )

        binding.errorContainer.btnRetry.setOnClickListener {
            searchByFilmName(binding.editTextEnterFilmName.query.toString())
        }
    }

    private fun searchByFilmName(filmName: String) {
        if (filmName.length > 1) {
            if (activeInternet()) {
                performSearch(filmName)
            } else {
                binding.rvFilms.visibility = View.GONE
                binding.errorContainer.root.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    getString(R.string.please_turn_on_the_internet),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun performSearch(textSearch: String) {
        val call = FilmClient.apiService.getFilms(textSearch, API_KEY)

        searchJob?.cancel()
        searchJob = scope.launch {
            binding.rvFilms.visibility = View.GONE
            binding.errorContainer.root.visibility = View.GONE
            binding.progressCircular.visibility = View.VISIBLE

            val result = withContext(Dispatchers.IO) {
                call.execute()
            }

            binding.rvFilms.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE

            if (result.isSuccessful) {
                val resultFilms = result.body()!!.films

                if (resultFilms.isNotEmpty()) {
                    val listFilms: MutableList<Film> = emptyList<Film>().toMutableList()
                    resultFilms.forEach {
                        listFilms.add(Film(0, it.title, it.description, it.posterPath, it.rating))
                    }
                    filmViewModel.addFilms(listFilms)
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.no_movie_found_for_your_request),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.rvFilms.visibility = View.GONE
                binding.errorContainer.root.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun activeInternet(): Boolean {
        return try {
            connectivityService.activeNetworkInfo?.isConnectedOrConnecting ?: false
        } catch (unused: Exception) {
            false
        }
    }
}