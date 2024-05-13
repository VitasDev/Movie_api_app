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
import com.example.film_api.QueryTextListener
import com.example.film_api.R
import com.example.film_api.datastate.DataState
import com.example.film_api.adapter.FilmAdapter
import com.example.film_api.databinding.ActivitySearchBinding
import com.example.film_api.viewmodel.FilmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val filmViewModel: FilmViewModel by viewModels()
    private lateinit var connectivityService: ConnectivityManager
    private lateinit var adapterFilms: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        adapterFilms = FilmAdapter()

        binding.rvFilms.adapter = adapterFilms
        binding.rvFilms.layoutManager = GridLayoutManager(this, 2)

        readFoundFilms()

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

        showCurrentFoundFilms()
    }

    private fun showCurrentFoundFilms() {
        lifecycleScope.launch {
            filmViewModel.findFilmState.collect {
                when (it) {
                    is DataState.EmptyData -> {
                        showToast(R.string.no_movie_found_for_your_request)
                    }
                    is DataState.Success -> {
                        binding.errorContainer.root.visibility = View.GONE
                        binding.progressCircular.visibility = View.GONE
                        binding.rvFilms.visibility = View.VISIBLE
                    }
                    is DataState.Error -> {
                        binding.progressCircular.visibility = View.GONE
                        binding.rvFilms.visibility = View.GONE
                        binding.errorContainer.root.visibility = View.VISIBLE
                        showToast(R.string.response_failed)
                    }
                    is DataState.Loading -> {
                        binding.errorContainer.root.visibility = View.GONE
                        binding.rvFilms.visibility = View.GONE
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun readFoundFilms() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filmViewModel.dataState.collect {
                    when(it) {
                        is DataState.EmptyData -> {
                            binding.progressCircular.visibility = View.GONE
                            showToast(R.string.list_is_empty)
                        }
                        is DataState.Success -> {
                            binding.progressCircular.visibility = View.GONE
                            binding.rvFilms.visibility = View.VISIBLE
                            adapterFilms.setData(it.data)
                        }
                        is DataState.Loading -> {
                            binding.rvFilms.visibility = View.GONE
                            binding.progressCircular.visibility = View.VISIBLE
                        }
                        is DataState.Error -> {}
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun searchByFilmName(filmName: String) {
        if (filmName.length > 1) {
            if (activeInternet()) {
                performSearch(filmName)
            } else {
                binding.rvFilms.visibility = View.GONE
                binding.errorContainer.root.visibility = View.VISIBLE
                showToast(R.string.please_turn_on_the_internet)
            }
        }
    }

    private fun performSearch(textSearch: String) {
        filmViewModel.getFilms(textSearch)
    }

    private fun activeInternet(): Boolean {
        return try {
            connectivityService.activeNetworkInfo?.isConnectedOrConnecting ?: false
        } catch (unused: Exception) {
            false
        }
    }

    private fun showToast(stringId: Int) {
        Toast.makeText(
            this,
            getString(stringId),
            Toast.LENGTH_LONG
        ).show()
    }
}