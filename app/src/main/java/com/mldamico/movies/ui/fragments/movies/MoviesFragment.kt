package com.mldamico.movies.ui.fragments.movies

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mldamico.movies.R
import com.mldamico.movies.adapters.MoviesAdapter
import com.mldamico.movies.databinding.FragmentMoviesBinding
import com.mldamico.movies.util.NetworkListener
import com.mldamico.movies.util.NetworkResult
import com.mldamico.movies.util.observeOnce
import com.mldamico.movies.viewmodels.MainViewModel
import com.mldamico.movies.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoviesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val mAdapter by lazy {
        MoviesAdapter()
    }
    private val args by navArgs<MoviesFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesViewModel: MoviesViewModel
    private var _binding: FragmentMoviesBinding? =null
    private val binding get() = _binding!!
    private var apiRequestForGenres = false
    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        moviesViewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.mainViewModel = mainViewModel
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.movies_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true

                searchView?.setOnQueryTextListener(this@MoviesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupRecyclerView()
        moviesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            moviesViewModel.backOnline = it
        }

        lifecycleScope.launchWhenStarted  {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect {status ->

                moviesViewModel.networkStatus = status
                moviesViewModel.showNetworkStatus()
                readDatabase()
            }
        }

        binding.swipeContainer.setOnRefreshListener {
            binding.swipeContainer.isRefreshing = false
            apiRequestForGenres = false
            requestApiData()
        }

        binding.moviesFab.setOnClickListener {
            if(moviesViewModel.networkStatus){
                findNavController().navigate(R.id.action_moviesFragment_to_moviesBottomSheet)
            } else {
                moviesViewModel.showNetworkStatus()
            }

        }

        return binding.root
    }

    private fun readDatabase() {

       lifecycleScope.launch {
           mainViewModel.readMovies.observeOnce(viewLifecycleOwner) { database ->
               if(database.isNotEmpty() && !args.backFromBottomSheet){
                   mAdapter.setData(database[0].movies)
                   hideShimmerEffect()
               } else if(args.backFromBottomSheet){
                   apiRequestForGenres = true
                   requestApiData()
               } else {
                   apiRequestForGenres = false
                   requestApiData()
               }
           }
       }
    }



    private fun requestApiData() {
        if(apiRequestForGenres){
            mainViewModel.getMoviesByGenre(moviesViewModel.applyQueriesGenres())
        } else {
            mainViewModel.getMovies(moviesViewModel.applyQueries())
        }

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

    private fun hideKeyboard(){
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
            hideKeyboard()
        }


        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query.isNullOrEmpty()){
            requestApiData()
            hideKeyboard()
        }
        return true
    }

    private fun searchApiData(query: String) {
        showShimmerEffect()
        mainViewModel.searchMovies(moviesViewModel.applySearchQuery(query))
        mainViewModel.searchMoviesResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success ->{
                    hideShimmerEffect()
                    val movies = response.data
                    movies?.let { mAdapter.setData(it) }
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


}