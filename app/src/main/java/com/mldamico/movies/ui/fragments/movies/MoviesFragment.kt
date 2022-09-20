package com.mldamico.movies.ui.fragments.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mldamico.movies.databinding.FragmentMoviesBinding


class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? =null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.recyclerView.showShimmer()


        return binding.root
    }


}