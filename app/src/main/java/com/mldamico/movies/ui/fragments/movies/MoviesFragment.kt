package com.mldamico.movies.ui.fragments.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mldamico.movies.MainViewModel
import com.mldamico.movies.adapters.MoviesAdapter
import com.mldamico.movies.databinding.FragmentMoviesBinding
import com.mldamico.movies.util.Constants.Companion.API_KEY
import com.mldamico.movies.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private val mAdapter by lazy {
        MoviesAdapter()
    }
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentMoviesBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.recyclerView.showShimmer()
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        requestApiData()

        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getMovies(applyQueries())
        mainViewModel.moviesResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
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

    private fun applyQueries():HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()
        queries["api_key"]=API_KEY
        queries["language"]="en-US"
        queries["page"]="1"
        return queries
    }

    private fun setupRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerView.hideShimmer()
    }


}