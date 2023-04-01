package com.longkd.cinema.moviedetail.data.remote.model.tv

data class TvShowCreditsResponse(
    val cast: List<CastTv>,
    val crew: List<CrewTv>,
    val id: Int
)
