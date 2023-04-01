package com.longkd.cinema.moviedetail.domain.usecase

import com.longkd.cinema.moviedetail.domain.MovieDetailRepository

class GetTvShowDetailsUseCase(
    private val movieDetailRepository: MovieDetailRepository
) {
    suspend operator fun invoke(showId: Int) = movieDetailRepository.getTvShowDetails(showId = showId)
}
