package com.longkd.cinema.search.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.longkd.cinema.GenresData
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.data.remote.pagingsource.MoviesPagingSource
import com.longkd.cinema.databinding.FragmentSearchBinding
import com.longkd.cinema.ui.MoviesBasicCardAdapter
import com.longkd.cinema.utils.QUERY_SEARCH_DELAY
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    override val fragmentConfiguration = FragmentConfiguration()
    private var isResumeFromVoice = false
    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val viewModel by viewModels<SearchFragmentViewModel>()
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private var searchJob: Job? = null

    private val moviesBasicCardAdapterListener =
        MoviesBasicCardAdapter.MoviesCardAdapterListener { movieBasicCardItem ->
            navToMovieDetailFragment(movieBasicCardItem.id)
        }
    private val moviesBasicCardAdapter = MoviesBasicCardAdapter(moviesBasicCardAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initResult()
        initUI()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if(!isResumeFromVoice) binding.searchEditText.text?.clear()
        else isResumeFromVoice = false
    }

    private fun initResult() {
        startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val res: ArrayList<String> =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.searchEditText.setText(Objects.requireNonNull(res)[0])
                isResumeFromVoice = true
            }
        }
    }

    private fun initUI() {
        showBottomNavbar()
        with(binding) {
            btnVoice.setOnClickListener {
                val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak))
                startForResult.launch(sttIntent)
            }
            GenresData.genres.forEach { genre ->
                categoriesTabLayout.addTab(categoriesTabLayout.newTab().setText(genre.name))
            }

            val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val allGenres = GenresData.genres.find {
                        it.id == 0
                    }?.name.orEmpty()
                    val selectedTabGenreName = GenresData.genres.find {
                        it.name == tab?.text
                    }?.name
                    viewModel.filterRecommendedMovies(selectedTabGenreName ?: allGenres)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
            categoriesTabLayout.addOnTabSelectedListener(onTabSelectedListener)

            recommendedMoviesRecyclerView.adapter = moviesBasicCardAdapter

            searchEditText.apply {
                doAfterTextChanged { searchQuery ->
                    searchQuery?.let {
                        searchJob?.cancel()
                        searchJob = lifecycleScope.launch {
                            delay(QUERY_SEARCH_DELAY)
                            if (searchQuery.trim().length > 1)
                                navToSearchResultFragment(searchQuery.toString())
                        }
                    }
                }
                //For submit button
                setOnEditorActionListener { textView, _, _ ->
                    if (textView.text.trim().length > 1) {
                        navToSearchResultFragment(textView.text.toString())
                    }
                    return@setOnEditorActionListener true
                }
            }
            seeAllTextView.setOnClickListener {
                navToRecommendedMoviesFragment()
            }

        }
    }

    private fun navToMovieDetailFragment(movieId: Int) {
        nav(SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(movieId))
    }

    private fun navToSearchResultFragment(searchQuery: String) {
        nav(SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(searchQuery))
    }

    private fun navToRecommendedMoviesFragment() {
        nav(SearchFragmentDirections.actionSearchFragmentToRecommendedMoviesFragment())
    }

    private fun initObservers() {
        viewLifecycleOwner.observe {
            viewModel.recommendedMoviesState.collectLatest { list ->
                moviesBasicCardAdapter.submitData(PagingData.from(list))
            }
        }
        viewLifecycleOwner.observe {
            viewModel.movieOfTheDayState.collectLatest { movieItem ->
                val typeText = when (movieItem.mediaType) {
                    MoviesPagingSource.TYPE_TV -> getString(R.string.tv)
                    MoviesPagingSource.TYPE_MOVIE -> getString(R.string.movie)
                    else -> getString(R.string.movie)
                }
                val imageUrl =
                    ImagesConfigData.secure_base_url + ImagesConfigData.poster_sizes?.get(2) + movieItem.poster_path
                binding.todayMovieItem.apply {
                    genreTextView.text = movieItem.genre
                    nameTextView.text = movieItem.title
                    yearTextView.text = movieItem.release_date
                    ratingTextView.text = movieItem.vote_average.toString()
                    mediaTypeTextView.text = typeText
                    Glide
                        .with(requireContext())
                        .load(imageUrl)
                        .centerCrop()
                        .into(posterImageView)
                    root.setOnClickListener {
                        navToMovieDetailFragment(movieItem.id)
                    }
                }
            }
        }
    }
}
