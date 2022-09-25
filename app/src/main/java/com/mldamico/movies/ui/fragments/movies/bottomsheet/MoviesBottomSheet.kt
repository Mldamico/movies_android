package com.mldamico.movies.ui.fragments.movies.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mldamico.movies.R
import com.mldamico.movies.databinding.MoviesBottomSheetBinding
import com.mldamico.movies.util.Constants.Companion.GENRE
import com.mldamico.movies.viewmodels.MoviesViewModel
import java.util.*

class MoviesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var moviesViewModel: MoviesViewModel
    private var _binding: MoviesBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var genreTypeChip = GENRE
    private var genreTypeChipId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoviesBottomSheetBinding.inflate(inflater, container, false)

        moviesViewModel.readGenreType.asLiveData().observe(viewLifecycleOwner) { value ->
            genreTypeChip = value.selectedGenre
            updateChip(value.selectedGenreId,binding.genreChipGroup)

        }

        binding.genreChipGroup.setOnCheckedStateChangeListener{ group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId.first())
            val selectedGenreType = chip.text.toString().lowercase(Locale.ROOT)
            genreTypeChip = selectedGenreType
            genreTypeChipId = selectedChipId.first()


        }

        binding.applyBtn.setOnClickListener {
            moviesViewModel.saveGenreType(genreTypeChip, genreTypeChipId)
            val action = MoviesBottomSheetDirections.actionMoviesBottomSheetToMoviesFragment(true)
            findNavController().navigate(action)
        }




        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}