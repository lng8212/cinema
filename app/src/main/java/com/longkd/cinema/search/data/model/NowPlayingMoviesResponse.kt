package com.longkd.cinema.search.data.model

import com.longkd.cinema.data.remote.model.MovieResult

class NowPlayingMoviesResponse(
    val page: Int,
    val results: List<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)
