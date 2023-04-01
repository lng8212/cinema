package com.longkd.cinema.search.ui

import androidx.lifecycle.viewModelScope
import com.longkd.cinema.GenresData
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.domain.model.mapToMovieBasicCardItem
import com.longkd.cinema.domain.model.mapToMovieBigCardItem
import com.longkd.cinema.search.domain.usecase.GetMovieOfTheDayUseCase
import com.longkd.cinema.search.domain.usecase.GetRecommendedMoviesUseCase
import com.longkd.cinema.ui.model.MovieBasicCardItem
import com.longkd.cinema.ui.model.MovieBigCardItem
import com.longkd.cinema.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val getRecommendedMoviesUseCase: GetRecommendedMoviesUseCase,
    private val getMovieOfTheDayUseCase: GetMovieOfTheDayUseCase
) : BaseViewModel() {

    private var _recommendedMoviesState = MutableStateFlow<List<MovieBasicCardItem>>(listOf())
    val recommendedMoviesState: StateFlow<List<MovieBasicCardItem>>
        get() = _recommendedMoviesState

    private var _movieOfTheDayState = MutableStateFlow<MovieBigCardItem>(
        MovieBigCardItem(
            id = 0,
            genre = "",
            poster_path = "",
            release_date = "",
            title = "",
            vote_average = 0.0,
            mediaType = ""
        )
    )
    val movieOfTheDayState: StateFlow<MovieBigCardItem>
        get() = _movieOfTheDayState


    init {
        getMovieOfTheDay()
        getRecommendedMovies()
    }

    private fun getRecommendedMovies(genreFilter: String? = null) {
        viewModelScope.launch {
            getRecommendedMoviesUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { movieList ->
                            if (genreFilter == null) {
                                _recommendedMoviesState.value = movieList.map { movie ->
                                    movie.mapToMovieBasicCardItem()
                                }
                            } else {
                                _recommendedMoviesState.value = movieList.map { movie ->
                                    movie.mapToMovieBasicCardItem()
                                }.filter {
                                    it.genre == genreFilter
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        //TODO error handling
                    }
                    is Resource.Loading -> {
                        //TODO loading handling
                    }
                }
            }
        }
    }

    private fun getMovieOfTheDay() {
        viewModelScope.launch {
            getMovieOfTheDayUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieOfTheDayState.value = movie.mapToMovieBigCardItem()
                        }
                    }
                    is Resource.Error -> {
                        //TODO error handling
                    }
                    is Resource.Loading -> {
                        //TODO loading handling
                    }
                }
            }
        }
    }

    fun filterRecommendedMovies(genre: String) {
        if (genre == GenresData.genres.find {
                it.id == 0
            }?.name) {
            getRecommendedMovies()
        } else {
            getRecommendedMovies(genreFilter = genre)
        }
    }

}
