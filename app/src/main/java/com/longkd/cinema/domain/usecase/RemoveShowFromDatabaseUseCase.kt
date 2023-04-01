package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository

class RemoveShowFromDatabaseUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(showId: Int) = moviesRepository.removeShowFromDatabase(showId)
}
