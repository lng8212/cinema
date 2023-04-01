package com.longkd.cinema.moviedetail.domain.usecase

import com.longkd.cinema.moviedetail.domain.MovieDetailRepository
import com.longkd.cinema.moviedetail.domain.model.Trailer
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.collectLatest

class GetShowTrailerUseCase(
    private val movieDetailRepository: MovieDetailRepository
) {

    suspend operator fun invoke(movieId: Int): Trailer {
        val trailers = movieDetailRepository.getShowTrailers(movieId)
        var filtered: List<Trailer>? = listOf()

        trailers.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    filtered = result.data?.filter {
                        it.type == "Trailer" &&
                                it.site == "YouTube"
                    }
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }
        return filtered?.firstOrNull() ?: Trailer(
            id = "",
            key = "",
            name = "",
            official = false,
            published_at = "",
            site = "",
            type = ""
        )

    }
}
