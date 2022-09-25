package com.mldamico.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mainImageView.load("https://image.tmdb.org/t/p/w500${args.result.posterPath}")
        binding.titleSingleMovieTextView.text = args.result.title
        binding.summaryTextView.text = args.result.overview
        binding.avgTextView.text = args.result.voteAverage.toString()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}