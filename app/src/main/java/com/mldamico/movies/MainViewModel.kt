package com.mldamico.movies

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mldamico.movies.data.Repository
import com.mldamico.movies.models.Movies
import com.mldamico.movies.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
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
            } catch (e: Exception){
                moviesResponse.value = NetworkResult.Error("Movies Not Found")
            }
        } else {
            moviesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
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