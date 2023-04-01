package com.longkd.cinema.home.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentPopularMoviesBinding
import com.longkd.cinema.ui.MovieBigCardItemAdapter
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PopularMoviesFragment : BaseFragment(R.layout.fragment_popular_movies) {

    private val toolbarConfiguration = ToolbarConfiguration(
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack,
        titleResId = R.string.most_popular_movie
    )

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private val binding by viewBinding(FragmentPopularMoviesBinding::bind)

    private val viewModel by viewModels<PopularMoviesViewModel>()

    private val bigCardAdapterListener = MovieBigCardItemAdapter.MoviesBigCardAdapterListener {
        navToMovieDetailFragment(it.id)
    }

    private val bigCardItemAdapter = MovieBigCardItemAdapter(bigCardAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
    }

    private fun initUI() {
        hideBottomNavbar()
        binding.moviesRecyclerView.adapter = bigCardItemAdapter
    }

    private fun navToMovieDetailFragment(movieId: Int) {
        nav(PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailFragment(movieId))
    }

    private fun initObserver() {
        viewLifecycleOwner.observe {
            viewModel.popularMoviesState.collectLatest {
                bigCardItemAdapter.submitData(it)
            }
        }
    }
}
