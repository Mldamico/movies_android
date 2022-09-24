package com.mldamico.movies.viewmodels

import android.app.Application
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

        viewModelScope.launch {
            readGenreType.collect {value ->
                genreType = value.selectedGenre
            }
        }

        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_GENRES] = genreType
        return queries
    }

}