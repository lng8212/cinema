package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.moviedetail.domain.model.MovieDetail

class InsertMovieToDatabaseUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieDetail: MovieDetail) = repository.insertMovieToDatabase(movieDetail)
}
