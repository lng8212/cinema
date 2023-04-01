package com.longkd.cinema.moviedetail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.moviedetail.domain.model.MovieDetail
import com.longkd.cinema.moviedetail.domain.usecase.GetMovieTrailerUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetShowTrailerUseCase
import com.longkd.cinema.utils.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieTrailerViewModel @Inject constructor(
    private val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    private val getShowTrailerUseCase: GetShowTrailerUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val movieDetail = savedStateHandle.getOrThrow<MovieDetail>("movieDetail")
    private val isMovie = savedStateHandle.getOrThrow<Boolean>("isMovie")

    private var _trailerId = MutableStateFlow<String?>(null)
    val trailerId: StateFlow<String?>
        get() = _trailerId

    init {
        getMovieTrailer()
    }

    private fun getMovieTrailer() {
        viewModelScope.launch {
            _trailerId.value = if (isMovie) {
                getMovieTrailerUseCase(movieDetail.id).key
            } else {
                getShowTrailerUseCase(movieDetail.id).key
            }
        }
    }
}
