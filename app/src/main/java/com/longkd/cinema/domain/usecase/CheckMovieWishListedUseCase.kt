package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository

class CheckMovieWishListedUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int) = moviesRepository.checkIsMovieWishListed(movieId)
}
