package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.moviedetail.domain.model.mapToWishListCardItem
import com.longkd.cinema.ui.model.WishListCardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWishListedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(): Flow<List<WishListCardItem>> {
        return moviesRepository.getWishListedMovies().map { list ->
            list.map {
                it.mapToWishListCardItem()
            }
        }
    }
}
