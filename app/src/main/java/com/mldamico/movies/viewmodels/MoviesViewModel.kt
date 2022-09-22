package com.mldamico.movies.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mldamico.movies.util.Constants
import com.mldamico.movies.util.Constants.Companion.QUERY_API_KEY
import com.mldamico.movies.util.Constants.Companion.QUERY_LANGUAGE
import com.mldamico.movies.util.Constants.Companion.QUERY_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(application: Application): AndroidViewModel(application) {


    fun applyQueries():HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_API_KEY]= Constants.API_KEY
        queries[QUERY_LANGUAGE]="en-US"
        queries[QUERY_PAGE]="1"
        return queries
    }

}