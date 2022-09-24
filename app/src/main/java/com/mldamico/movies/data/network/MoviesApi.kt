package com.mldamico.movies.data.network

import com.mldamico.movies.models.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviesApi {

    @GET("/3/movie/now_playing")
    suspend fun getMovies(@QueryMap queries: Map<String, String>): Response<Movies>

    @GET("/3/discover/movie")
    suspend fun getMovieByGenre(@QueryMap queries: Map<String, String>): Response<Movies>
}