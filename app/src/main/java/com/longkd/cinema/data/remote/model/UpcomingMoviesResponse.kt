package com.longkd.cinema.data.remote.model

data class UpcomingMoviesResponse(
    val page: Int,
    val results: List<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)
