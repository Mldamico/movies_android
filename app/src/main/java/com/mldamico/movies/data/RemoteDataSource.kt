package com.mldamico.movies.data

import com.mldamico.movies.data.network.MoviesApi
import com.mldamico.movies.models.Movies
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesApi: MoviesApi) {

    suspend fun getMovies(queries: Map<String, String>): Response<Movies> {
        return moviesApi.getMovies(queries)
    }
}