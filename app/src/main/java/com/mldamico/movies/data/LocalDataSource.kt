package com.mldamico.movies.data

import com.mldamico.movies.data.database.MoviesDao
import com.mldamico.movies.data.database.MoviesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val moviesDao: MoviesDao) {

    fun readDatabase(): Flow<List<MoviesEntity>> {
        return moviesDao.readMovies()
    }

    suspend fun insertMovies(moviesEntity: MoviesEntity){
        moviesDao.insertMovies(moviesEntity)
    }
}