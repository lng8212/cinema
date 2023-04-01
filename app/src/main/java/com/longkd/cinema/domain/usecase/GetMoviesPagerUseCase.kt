package com.longkd.cinema.domain.usecase

import com.longkd.cinema.data.remote.pagingsource.PagingDataType
import com.longkd.cinema.domain.repository.MoviesRepository

class GetMoviesPagerUseCase(
    private val moviesRepository: MoviesRepository
) {
    operator fun invoke(pagingDataType: PagingDataType<Any>) = moviesRepository.getMoviesPager(pagingDataType)
}
