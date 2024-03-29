package com.longkd.cinema.home.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.longkd.cinema.GenresData
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.data.remote.pagingsource.PagingDataType
import com.longkd.cinema.domain.model.mapToCarouselMovieItem
import com.longkd.cinema.domain.model.mapToMovieBasicCardItem
import com.longkd.cinema.domain.usecase.GetMoviesPagerUseCase
import com.longkd.cinema.home.ui.model.CarouselMovieItem
import com.longkd.cinema.home.usecase.GetCarouselMoviesUseCase
import com.longkd.cinema.ui.model.MovieBasicCardItem
import com.longkd.cinema.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesPagerUseCase: GetMoviesPagerUseCase,
    private val getCarouselMoviesUseCase: GetCarouselMoviesUseCase
) : BaseViewModel() {

    private val _popularMoviesState = MutableStateFlow<PagingData<MovieBasicCardItem>>(PagingData.empty())
    val popularMoviesState: StateFlow<PagingData<MovieBasicCardItem>>
        get() = _popularMoviesState

    private val _upcomingMoviesState = MutableStateFlow<List<CarouselMovieItem>>(listOf())
    val upcomingMoviesState: StateFlow<List<CarouselMovieItem>>
        get() = _upcomingMoviesState

    private val _topRatedMoviesState = MutableStateFlow<PagingData<MovieBasicCardItem>>(PagingData.empty())
    val topRatedMoviesState: StateFlow<PagingData<MovieBasicCardItem>>
        get() = _topRatedMoviesState

    init {
        getPopularMovies()
        getUpcomingMovies()
        getTopRatedMovies()
    }

    private fun getPopularMovies(genreFilter: String? = null) {
        viewModelScope.launch {
            getMoviesPagerUseCase(pagingDataType = PagingDataType.PopularMovies())
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { movieList ->
                    if (genreFilter == null) {
                        _popularMoviesState.value = movieList.map { movie ->
                            movie.mapToMovieBasicCardItem()
                        }
                    } else {
                        _popularMoviesState.value = movieList.map { movie ->
                            movie.mapToMovieBasicCardItem()
                        }.filter {
                            it.genre == genreFilter
                        }
                    }
                }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            getCarouselMoviesUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { listOfMovies ->
                            _upcomingMoviesState.value = listOfMovies.map { movie ->
                                movie.mapToCarouselMovieItem()
                            }
                        }
                    }
                    is Resource.Error -> {
                        //TODO error state
                    }
                    is Resource.Loading -> {
                        //TODO handle loading state
                    }
                }
            }
        }
    }

    private fun getTopRatedMovies(genreFilter: String? = null) {
        viewModelScope.launch {
            getMoviesPagerUseCase(pagingDataType = PagingDataType.TopRatedMovies())
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { movieList ->
                    if (genreFilter == null) {
                        _topRatedMoviesState.value = movieList.map { movie ->
                            movie.mapToMovieBasicCardItem()
                        }
                    } else {
                        _topRatedMoviesState.value = movieList.map { movie ->
                            movie.mapToMovieBasicCardItem()
                        }.filter {
                            it.genre == genreFilter
                        }
                    }
                }
        }
    }

    fun filterPopularMovies(genreName: String) {
        if (genreName == GenresData.genres.find {
                it.id == 0
            }?.name) {
            getPopularMovies()
        } else {
            getPopularMovies(genreFilter = genreName)
        }
    }

    fun filterTopRatedMovies(genreName: String) {
        if (genreName == GenresData.genres.find {
                it.id == 0
            }?.name) {
            getTopRatedMovies()
        } else {
            getTopRatedMovies(genreFilter = genreName)
        }
    }


}
