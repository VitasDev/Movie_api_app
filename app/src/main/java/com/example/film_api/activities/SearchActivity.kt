package com.example.film_api.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.film_api.Constants
import com.example.film_api.Constants.API_KEY
import com.example.film_api.R
import com.example.film_api.adapter.FilmAdapter
import com.example.film_api.databinding.ActivitySearchBinding
import com.example.film_api.interfaces.OnItemClickListener
import com.example.film_api.model.Films
import com.example.film_api.retrofit.FilmClient
import com.example.film_api.viewmodel.FilmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var filmViewModel: FilmViewModel
    private lateinit var connectivityService: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextEnterFilmName.addTextChangedListener(textWatcher)
        connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val adapterFilms = FilmAdapter(object : OnItemClickListener {
            override fun onItemClick(film: Films) {
                val intent = Intent(this@SearchActivity, InfoFilmActivity::class.java)
                intent.putExtra(Constants.EXTRA_OBJ, film ?: "")
                startActivity(intent)
            }
        })

        binding.rvFilms.adapter = adapterFilms
        binding.rvFilms.layoutManager = GridLayoutManager(this, 2)

        filmViewModel = ViewModelProvider(this)[FilmViewModel::class.java]
        filmViewModel.readAllFilms.observe(this) { films ->
            adapterFilms.setData(films)
        }

        binding.btnClear.setOnClickListener {
            binding.editTextEnterFilmName.setText("")
        }

        binding.editTextEnterFilmName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val textSearch = binding.editTextEnterFilmName.text.toString()
                if (activeInternet()) {
                    if (textSearch.length > 1) {
                        performSearch(textSearch)
                    } else {
                        Toast.makeText(this, getString(R.string.please_enter_film_name), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.please_turn_on_the_internet), Toast.LENGTH_LONG).show()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {
            if (c.toString().trim().isEmpty()) {
                binding.btnClear.visibility = View.GONE
            } else {
                binding.btnClear.visibility = View.VISIBLE
            }
        }
        override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {}
    }

    private fun performSearch(textSearch: String) {
        val call = FilmClient.apiService.getFilms(API_KEY, textSearch)

        scope.launch {
            binding.rvFilms.visibility = View.INVISIBLE
            binding.progressCircular.visibility = View.VISIBLE

            val result = withContext(Dispatchers.IO) {
                call.execute()
            }

            binding.rvFilms.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE

            if (result.isSuccessful) {
                val resultFilms = result.body()!!.films

                if (resultFilms.isNotEmpty()) {
                    val listFilms: MutableList<Films> = emptyList<Films>().toMutableList()
                    resultFilms.forEach {
                        listFilms.add(Films(0, it.title, it.description, it.posterPath, it.rating))
                    }
                    filmViewModel.addFilms(listFilms)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.no_movie_found_for_your_request), Toast.LENGTH_SHORT).show()
                }
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