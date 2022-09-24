package com.mldamico.movies.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.mldamico.movies.data.DataStoreRepository
import com.mldamico.movies.data.Repository
import com.mldamico.movies.data.database.MoviesEntity
import com.mldamico.movies.models.Movies
import com.mldamico.movies.util.Constants
import com.mldamico.movies.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {
    val readMovies: LiveData<List<MoviesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertMovies(moviesEntity: MoviesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertMovies(moviesEntity)
    }

    var moviesResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()

    fun getMovies(queries: Map<String, String>) = viewModelScope.launch {
        getMoviesSafeCall(queries)
    }

    private suspend fun getMoviesSafeCall(queries: Map<String, String>) {
        moviesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getMovies(queries)
                moviesResponse.value = NetworkResult.Success(response.body()!!)
                val movies = moviesResponse.value!!.data
                if(movies !=null){
                    offlineCacheMovies(movies)
                }
            } catch (e: Exception){
                moviesResponse.value = NetworkResult.Error("Movies Not Found")
            }
        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    fun getMoviesByGenre(queries: Map<String, String>) = viewModelScope.launch {
        getMoviesByGenreSafeCall(queries)


    }

    private suspend fun getMoviesByGenreSafeCall(queries: Map<String, String>){
        moviesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.getMoviesByGenre(queries)
                moviesResponse.value = NetworkResult.Success(response.body()!!)
                val movies = moviesResponse.value!!.data
                if(movies !=null){
                    offlineCacheMovies(movies)
                }
            } catch (e: Exception){
                moviesResponse.value = NetworkResult.Error("Movies Not Found")
            }
        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheMovies(movies: Movies) {
        val moviesEntity = MoviesEntity(movies)
        insertMovies(moviesEntity)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> return false
        }
    }
}