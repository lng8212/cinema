package com.longkd.cinema.search.domain.usecase

import com.longkd.cinema.domain.model.Movie
import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.search.domain.SearchRepository
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetRecommendedMoviesUseCase(
    private val searchRepository: SearchRepository,
    private val moviesRepository: MoviesRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Movie>>> {
        val randomMovieId = moviesRepository.getRandomWishListedMovieId()
        return searchRepository.getRecommendedMovies(randomMovieId)
    }
}
