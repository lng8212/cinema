package com.longkd.cinema.moviedetail.ui.model

import com.longkd.cinema.moviedetail.domain.model.MovieDetail

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false
)
