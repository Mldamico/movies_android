package com.mldamico.movies.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mldamico.movies.models.Movies

class MoviesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun moviesToString(movies:Movies): String {
        return gson.toJson(movies)
    }

    @TypeConverter
    fun stringToMovies(data: String): Movies {
        val listType = object : TypeToken<Movies>() {}.type
        return gson.fromJson(data, listType)
    }


}