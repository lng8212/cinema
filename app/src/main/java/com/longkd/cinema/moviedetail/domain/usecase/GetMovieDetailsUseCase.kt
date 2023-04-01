package com.longkd.cinema.moviedetail.domain.usecase

import com.longkd.cinema.moviedetail.domain.MovieDetailRepository

class GetMovieDetailsUseCase(
    private val movieDetailRepository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: Int) = movieDetailRepository.getMovieDetails(movieId)
}
