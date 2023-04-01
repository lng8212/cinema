package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository

class RemoveMovieFromDatabaseUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int) = moviesRepository.removeMovieFromDatabase(movieId)
}
