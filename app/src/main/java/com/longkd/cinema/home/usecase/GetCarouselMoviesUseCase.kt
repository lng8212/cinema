package com.longkd.cinema.home.usecase

import com.longkd.cinema.domain.model.Movie
import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetCarouselMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Movie>>> {
        return moviesRepository.getUpcomingMovies()
    }
}
