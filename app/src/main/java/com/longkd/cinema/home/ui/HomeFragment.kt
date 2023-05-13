package com.longkd.cinema.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.longkd.cinema.GenresData
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.databinding.FragmentHomeBinding
import com.longkd.cinema.ui.MoviesBasicCardAdapter
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    override val fragmentConfiguration = FragmentConfiguration()

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val popularMoviesAdapterListener =
        MoviesBasicCardAdapter.MoviesCardAdapterListener { movieBasicCardItem ->
            navToMovieDetailFragment(movieBasicCardItem.id)
        }

    private val topRatedMoviesAdapterListener =
        MoviesBasicCardAdapter.MoviesCardAdapterListener { movieBasicCardItem ->
            navToMovieDetailFragment(movieBasicCardItem.id)
        }

    private val carouselAdapterListener = MoviesCarouselAdapter.MoviesCarouselAdapterListener { carouselMovieItem ->
        navToMovieDetailFragment(carouselMovieItem.id)
    }

    private val topRatedMoviesAdapter = MoviesBasicCardAdapter(topRatedMoviesAdapterListener)

    private val upcomingMoviesCarouselAdapter = MoviesCarouselAdapter(carouselAdapterListener)

    private val popularMoviesAdapter = MoviesBasicCardAdapter(popularMoviesAdapterListener)

    //This variable is used for saving state of selected tab and restore it in onResume()
    private var selectedTabPosition = 0

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val allGenres = GenresData.genres.find {
                it.id == 0
            }?.name.orEmpty()
            val selectedTabGenreName = GenresData.genres.find {
                it.name == tab?.text
            }?.name
            viewModel.filterPopularMovies(selectedTabGenreName ?: allGenres)
            viewModel.filterTopRatedMovies(selectedTabGenreName ?: allGenres)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private lateinit var auth: FirebaseAuth

    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        binding.categoriesTabLayout.selectTab(binding.categoriesTabLayout.getTabAt(selectedTabPosition))
        binding.categoriesTabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    override fun onPause() {
        super.onPause()
        selectedTabPosition = binding.categoriesTabLayout.selectedTabPosition
        binding.categoriesTabLayout.removeOnTabSelectedListener(onTabSelectedListener)
    }

    private fun initUI() {
        showBottomNavbar()
        with(binding) {
            carouselRecyclerView.apply {
                this.adapter = upcomingMoviesCarouselAdapter
                set3DItem(true)
                setAlpha(true)
            }
            popularMoviesRecyclerView.adapter = popularMoviesAdapter
            topRatedMoviesRecyclerView.adapter = topRatedMoviesAdapter

            seeAllTextView.setOnClickListener {
                navToPopularMoviesFragment()
            }

            Glide.with(requireContext())
                .load(currentUser?.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_person_24)
                .into(personImageView)

            helloTextView.text = getString(R.string.hello_with_comma, currentUser?.displayName ?: "")

            GenresData.genres.forEach { genre ->
                categoriesTabLayout.addTab(categoriesTabLayout.newTab().setText(genre.name))
            }
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.observe {
            viewModel.popularMoviesState.collectLatest {
                popularMoviesAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.observe {
            viewModel.upcomingMoviesState.collectLatest {
                upcomingMoviesCarouselAdapter.submitList(it)
            }
        }
        viewLifecycleOwner.observe {
            viewModel.topRatedMoviesState.collectLatest {
                topRatedMoviesAdapter.submitData(it)
            }
        }
    }

    private fun navToMovieDetailFragment(movieId: Int) {
        nav(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movieId))
    }

    private fun navToPopularMoviesFragment() {
        nav(HomeFragmentDirections.actionHomeFragmentToPopularMoviesFragment())
    }
}
