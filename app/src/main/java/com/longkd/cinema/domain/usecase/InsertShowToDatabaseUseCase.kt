package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.moviedetail.domain.model.TvShowDetail

class InsertShowToDatabaseUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(tvShowDetail: TvShowDetail) = moviesRepository.insertShowToDatabase(tvShowDetail)
}
