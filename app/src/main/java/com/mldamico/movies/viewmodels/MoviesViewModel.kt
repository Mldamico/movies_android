package com.mldamico.movies.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mldamico.movies.data.DataStoreRepository
import com.mldamico.movies.util.Constants
import com.mldamico.movies.util.Constants.Companion.GENRE
import com.mldamico.movies.util.Constants.Companion.QUERY_API_KEY
import com.mldamico.movies.util.Constants.Companion.QUERY_GENRES
import com.mldamico.movies.util.Constants.Companion.QUERY_LANGUAGE
import com.mldamico.movies.util.Constants.Companion.QUERY_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {
    private var genreType = ""

    val readGenreType = dataStoreRepository.readGenreType

    fun saveGenreType(genreType: String, genreTypeId: Int) = viewModelScope.launch(Dispatchers.IO) {
    dataStoreRepository.saveGenreType(genreType, genreTypeId)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_LANGUAGE] = "en-US"
        queries[QUERY_PAGE] = "1"
        return queries
    }

    fun applyQueriesGenres(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        var genreId =""
        viewModelScope.launch {
            readGenreType.collect {value ->
                genreType = value.selectedGenre
            }
        }

        when(genreType) {

            "action" -> {
                genreId = "28"
            }
            "adventure" -> {
                genreId = "12"
            }
            "animation" -> {
                genreId = "16"
            }
            "comedy" -> {
                genreId = "35"
            }
            "crime" -> {
                genreId = "80"
            }
            "documentary" -> {
                genreId = "99"
            }
            "drama" -> {
                genreId = "18"
            }
            "family" -> {
                genreId = "10751"
            }
            "fantasy" -> {
                genreId = "14"
            }
            "history" -> {
                genreId = "36"
            }
            "horror" -> {
                genreId = "27"
            }
            "music" -> {
                genreId = "10402"
            }
            "mystery" -> {
                genreId = "9648"
            }
            "romance" -> {
                genreId = "10749"
            }
            "science Fiction" -> {
                genreId = "878"
            }
            "tv movie" -> {
                genreId = "10770"
            }
            "thriller" -> {
                genreId = "53"
            }
            "war" -> {
                genreId = "10752"
            }
            "western" -> {
                genreId = "37"
            }
        }
        Log.d("MoviesViewModel", genreType)
        Log.d("MoviesViewModel", genreId)

        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_GENRES] = genreId
        return queries
    }

}