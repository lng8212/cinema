package com.longkd.cinema.search.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentRecommendedMoviesBinding
import com.longkd.cinema.ui.MovieBigCardItemAdapter
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RecommendedMoviesFragment : BaseFragment(R.layout.fragment_recommended_movies) {

    private val toolbarConfiguration = ToolbarConfiguration(
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack,
        titleResId = R.string.recommended
    )

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private val binding by viewBinding(FragmentRecommendedMoviesBinding::bind)

    private val viewModel by viewModels<RecommendedMoviesViewModel>()

    private val bigCardAdapterListener = MovieBigCardItemAdapter.MoviesBigCardAdapterListener {
        navToMovieDetailFragment(it.id)
    }

    private val bigCardItemAdapter = MovieBigCardItemAdapter(bigCardAdapterListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        hideBottomNavbar()
        binding.moviesRecyclerView.adapter = bigCardItemAdapter
    }

    private fun initObservers() {
        viewLifecycleOwner.observe {
            viewModel.recommendedMoviesState.collectLatest {
                bigCardItemAdapter.submitData(it)
            }
        }

    }

    private fun navToMovieDetailFragment(movieId: Int) {
        nav(RecommendedMoviesFragmentDirections.actionRecommendedMoviesFragmentToMovieDetailFragment(movieId))
    }
}
