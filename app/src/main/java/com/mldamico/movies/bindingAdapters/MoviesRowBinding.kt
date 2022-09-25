package com.mldamico.movies.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.mldamico.movies.R
import com.mldamico.movies.models.Result
import com.mldamico.movies.ui.fragments.movies.MoviesFragmentDirections

class MoviesRowBinding {
    companion object {

        @BindingAdapter("onMovieClickListener")
        @JvmStatic
        fun onMovieClickListener(movieRowLayout: ConstraintLayout, result: Result) {
            movieRowLayout.setOnClickListener {
                try {
                    val action = MoviesFragmentDirections.actionMoviesFragmentToOverviewActivity(result)
                    movieRowLayout.findNavController().navigate(action)
                } catch (e: Exception){
                    Log.d("onMovieClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String){

            imageView.load("https://image.tmdb.org/t/p/w300$imageUrl") {
                crossfade(600)
                error(R.drawable.ic_broken_image)
            }
        }

        @BindingAdapter("setVoteAverage")
        @JvmStatic
        fun setVoteAverage(textView: TextView, voteAverage: Double) {
            textView.text = voteAverage.toString()

        }

        @BindingAdapter("setVoteCount")
        @JvmStatic
        fun setVoteCount(textView: TextView, voteCount: Int) {
            textView.text = voteCount.toString()
        }

        @BindingAdapter("applyChildColor")
        @JvmStatic
        fun applyChildColor(view: View, child: Boolean) {
            if (child) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(view.context, R.color.green)
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }

    }
}