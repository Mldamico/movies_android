package com.mldamico.movies.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.mldamico.movies.util.Constants.Companion.GENRE
import com.mldamico.movies.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.mldamico.movies.util.Constants.Companion.PREFERENCES_GENRE
import com.mldamico.movies.util.Constants.Companion.PREFERENCES_GENRE_ID
import com.mldamico.movies.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedGenre = stringPreferencesKey(PREFERENCES_GENRE)
        val selectedGenreId = intPreferencesKey(PREFERENCES_GENRE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)

    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    suspend fun saveGenreType(
        genre: String,
        genreId: Int,

    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedGenre] = genre
            preferences[PreferenceKeys.selectedGenreId] = genreId

        }
    }

    suspend fun saveBackOnline(backOnline: Boolean){
        context.dataStore.edit {preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readGenreType: Flow<GenreType> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedGenre] ?: GENRE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedGenreId] ?: 0

            GenreType(
                selectedMealType, selectedMealTypeId
            )
        }

    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?:  false
            backOnline
        }
}

data class GenreType(
    val selectedGenre: String,
    val selectedGenreId: Int,
)