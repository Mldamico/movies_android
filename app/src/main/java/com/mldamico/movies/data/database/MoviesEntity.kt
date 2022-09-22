package com.mldamico.movies.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mldamico.movies.models.Movies
import com.mldamico.movies.util.Constants.Companion.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity(var movies: Movies) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}