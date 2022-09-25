package com.mldamico.movies.data

import android.util.Log
import com.mldamico.movies.data.network.MoviesApi
import com.mldamico.movies.models.Movies
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesApi: MoviesApi) {

    suspend fun getMovies(queries: Map<String, String>): Response<Movies> {
        return moviesApi.getMovies(queries)
    }

    suspend fun getMoviesByGenre(queries: Map<String, String>): Response<Movies>{
        return moviesApi.getMovieByGenre(queries)
    }

    suspend fun searchQuery(queries: Map<String, String>): Response<Movies>{
        return moviesApi.searchMovie(queries)
    }
}