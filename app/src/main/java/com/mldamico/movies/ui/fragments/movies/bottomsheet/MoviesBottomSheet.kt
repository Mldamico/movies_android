package com.mldamico.movies.ui.fragments.movies.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mldamico.movies.R
import com.mldamico.movies.util.Constants.Companion.GENRE
import com.mldamico.movies.viewmodels.MoviesViewModel
import java.util.*

class MoviesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var moviesViewModel: MoviesViewModel
    private var genreTypeChip = GENRE
    private var genreTypeChipId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.movies_bottom_sheet, container, false)

        moviesViewModel.readGenreType.asLiveData().observe(viewLifecycleOwner) { value ->
            genreTypeChip = value.selectedGenre
            updateChip(value.selectedGenreId, mView.findViewById<ChipGroup>(R.id.genre_chipGroup))

        }

        mView.findViewById<ChipGroup>(R.id.genre_chipGroup).setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedGenreType = chip.text.toString().lowercase(Locale.ROOT)
            genreTypeChip = selectedGenreType
            genreTypeChipId = selectedChipId.first()
//            when(genreTypeChip) {
//                "Action" -> {
//                    genreTypeChipId = 28
//                }
//                "Adventure" -> {
//                    genreTypeChipId = 12
//                }
//                "Animation" -> {
//                    genreTypeChipId = 16
//                }
//                "Comedy" -> {
//                    genreTypeChipId = 35
//                }
//                "Crime" -> {
//                    genreTypeChipId = 80
//                }
//                "Documentary" -> {
//                    genreTypeChipId = 99
//                }
//                "Drama" -> {
//                    genreTypeChipId = 18
//                }
//                "Family" -> {
//                    genreTypeChipId = 10751
//                }
//                "Fantasy" -> {
//                    genreTypeChipId = 14
//                }
//                "History" -> {
//                    genreTypeChipId = 36
//                }
//                "Horror" -> {
//                    genreTypeChipId = 27
//                }
//                "Music" -> {
//                    genreTypeChipId = 10402
//                }
//                "Mystery" -> {
//                    genreTypeChipId = 9648
//                }
//                "Romance" -> {
//                    genreTypeChipId = 10749
//                }
//                "Science Fiction" -> {
//                    genreTypeChipId = 878
//                }
//                "TV Movie" -> {
//                    genreTypeChipId = 10770
//                }
//                "Thriller" -> {
//                    genreTypeChipId = 53
//                }
//                "War" -> {
//                    genreTypeChipId = 10752
//                }
//                "Western" -> {
//                    genreTypeChipId = 37
//                }
//            }

        }

        mView.findViewById<AppCompatButton>(R.id.apply_btn).setOnClickListener {
            moviesViewModel.saveGenreType(genreTypeChip, genreTypeChipId)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup:ChipGroup) {
        if(chipId != 0){
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true

            } catch (e: Exception) {
                Log.d("moviesBottomSheet", e.message.toString())
            }
        }
    }

}