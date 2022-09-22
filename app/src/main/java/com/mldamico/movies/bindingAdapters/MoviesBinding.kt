package com.mldamico.movies.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mldamico.movies.data.database.MoviesEntity
import com.mldamico.movies.models.Movies
import com.mldamico.movies.util.NetworkResult

class MoviesBinding {

    companion object{
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageView(imageView: ImageView, apiResponse: NetworkResult<Movies>?, database: List<MoviesEntity>?){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading){
                imageView.visibility = View.INVISIBLE
            } else if(apiResponse is NetworkResult.Success){
                imageView.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponseErrorText", "readDatabaseErrorText", requireAll = true)
        @JvmStatic
        fun errorTextView(textView: TextView, apiResponse: NetworkResult<Movies>?, database: List<MoviesEntity>?){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading){
                textView.visibility = View.INVISIBLE
            } else if(apiResponse is NetworkResult.Success){
                textView.visibility = View.INVISIBLE
            }
        }
    }

}