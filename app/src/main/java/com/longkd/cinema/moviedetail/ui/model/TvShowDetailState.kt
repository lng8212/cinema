package com.longkd.cinema.moviedetail.ui.model

import com.longkd.cinema.moviedetail.domain.model.TvShowDetail

data class TvShowDetailState(
    val tvShowDetail: TvShowDetail? = null,
    val isLoading: Boolean = false
)
