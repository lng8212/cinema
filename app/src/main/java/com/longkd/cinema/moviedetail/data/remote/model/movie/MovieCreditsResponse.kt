package com.longkd.cinema.moviedetail.data.remote.model.movie

data class MovieCreditsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)
