package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.moviedetail.domain.model.mapToWishListCardItem
import com.longkd.cinema.ui.model.WishListCardItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetWishListedMediaUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(): Flow<List<WishListCardItem>> {
        val mediaList = mutableListOf<WishListCardItem>()
        moviesRepository.getWishListedMovies().map { list ->
            list.map {
                it.mapToWishListCardItem()
            }
        }.collectLatest {
            mediaList.addAll(it)
        }
        moviesRepository.getWishListedShows().map { list ->
            list.map {
                it.mapToWishListCardItem()
            }
        }.collectLatest {
            mediaList.addAll(it)
        }
        return flowOf(mediaList)
    }
}
