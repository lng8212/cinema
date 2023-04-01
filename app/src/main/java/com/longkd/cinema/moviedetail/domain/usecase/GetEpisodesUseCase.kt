package com.longkd.cinema.moviedetail.domain.usecase

import com.longkd.cinema.moviedetail.domain.MovieDetailRepository

class GetEpisodesUseCase(
    private val movieDetailRepository: MovieDetailRepository
) {
    suspend operator fun invoke(seasonNumber: Int, showId: Int) =
        movieDetailRepository.getTvShowSeasonEpisodes(showId = showId, seasonNumber = seasonNumber)
}
