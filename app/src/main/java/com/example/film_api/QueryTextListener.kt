package com.example.film_api

import android.content.Context
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QueryTextListener(
    private val context: Context,
    lifecycle: Lifecycle,
    private val queryTextChange: (String?) -> Unit
) : SearchView.OnQueryTextListener {

    private var delayPeriod: Long = 700
    private val coroutineScope = lifecycle.coroutineScope
    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!!.length <= 1) {
            Toast.makeText(
                context,
                context.getString(R.string.please_enter_film_name),
                Toast.LENGTH_SHORT
            ).show()
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(delayPeriod)
                queryTextChange(newText)
            }
        }
        return false
    }
}