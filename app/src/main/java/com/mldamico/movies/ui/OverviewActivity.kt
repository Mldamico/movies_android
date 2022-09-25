package com.mldamico.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import coil.load

import com.mldamico.movies.R
import com.mldamico.movies.databinding.ActivityOverviewBinding


class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding
    private val args by navArgs<OverviewActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val listAdapter: ArrayAdapter<String>
        val newList = mutableListOf<String>()
        for(item in args.result.genres){
            newList.add(mapGenreIdToString(item))
        }
        Log.d("overview",newList.toString())

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mainImageView.load("https://image.tmdb.org/t/p/w500${args.result.posterPath}")
        binding.titleSingleMovieTextView.text = args.result.title
        binding.summaryTextView.text = args.result.overview
        binding.avgTextView.text = args.result.voteAverage.toString()
        binding.releaseDateOverview.text = args.result.releaseDate
        binding.languageOverview.text = args.result.originalLanguage

        listAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, newList)
        binding.genreList.adapter = listAdapter

    }

    private fun mapGenreIdToString(genreId: Int): String{
        var genreType:String =""
        when (genreId) {
            28 -> {
                genreType = "Action"
            }
            12 -> {
                genreType = "Adventure"
            }
            16 -> {
                genreType = "Animation"
            }
            35 -> {
                genreType = "Comedy"
            }
            80 -> {
                genreType = "Crime"
            }
            99 -> {
                genreType = "Documentary"
            }
            18 -> {
                genreType = "Drama"
            }
            10751-> {
                genreType = "Family"
            }
            14 -> {
                genreType = "Fantasy"
            }
            36-> {
                genreType = "History"
            }
             27-> {
                genreType = "Horror"
            }
            10402 -> {
                genreType = "Music"
            }
            9648 -> {
                genreType = "Mistery"
            }
            10749 -> {
                genreType = "Romance"
            }
            878 -> {
                genreType = "Science Fiction"
            }
           10770 -> {
                genreType = "TV Movie"
            }
            53 -> {
                genreType = "Thriller"
            }
            10752 -> {
                genreType = "War"
            }
            37 -> {
                genreType = "Western"
            }
        }

        return genreType

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}