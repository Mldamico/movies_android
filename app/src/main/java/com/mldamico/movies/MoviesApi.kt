package com.mldamico.movies

import com.mldamico.movies.models.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviesApi {

    @GET("/movie/now_playing")
    suspend fun getMovies(@QueryMap queries: Map<String, String>): Response<Movies>
}