package com.longkd.cinema.home.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.data.remote.pagingsource.PagingDataType
import com.longkd.cinema.domain.model.mapToMovieBigCardItem
import com.longkd.cinema.domain.usecase.GetMoviesPagerUseCase
import com.longkd.cinema.ui.model.MovieBigCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val getMoviesPagerUseCase: GetMoviesPagerUseCase
) : BaseViewModel() {

    private val _popularMoviesState = MutableStateFlow<PagingData<MovieBigCardItem>>(PagingData.empty())
    val popularMoviesState: StateFlow<PagingData<MovieBigCardItem>>
        get() = _popularMoviesState


    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            getMoviesPagerUseCase(pagingDataType = PagingDataType.PopularMovies())
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { movieList ->
                    _popularMoviesState.value = movieList.map {
                        it.mapToMovieBigCardItem()
                    }
                }
        }
    }
}
