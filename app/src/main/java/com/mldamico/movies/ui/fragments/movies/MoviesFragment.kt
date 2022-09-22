package com.mldamico.movies.ui.fragments.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mldamico.movies.viewmodels.MainViewModel
import com.mldamico.movies.adapters.MoviesAdapter
import com.mldamico.movies.databinding.FragmentMoviesBinding
import com.mldamico.movies.util.Constants.Companion.API_KEY
import com.mldamico.movies.util.NetworkResult
import com.mldamico.movies.util.observeOnce
import com.mldamico.movies.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private val mAdapter by lazy {
        MoviesAdapter()
    }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel
    private var _binding: FragmentMoviesBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        readDatabase()

        return binding.root
    }

    private fun readDatabase() {
       lifecycleScope.launch {
           mainViewModel.readMovies.observeOnce(viewLifecycleOwner) { database ->
               if(database.isNotEmpty()){
                   mAdapter.setData(database[0].movies)
                   hideShimmerEffect()
               } else {
                   requestApiData()
               }
           }
       }
    }

    private fun requestApiData() {
        mainViewModel.getMovies(moviesViewModel.applyQueries())
        mainViewModel.moviesResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        }
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readMovies.observe(viewLifecycleOwner) { database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].movies)
                }
            }
        }
    }


    private fun setupRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }


}