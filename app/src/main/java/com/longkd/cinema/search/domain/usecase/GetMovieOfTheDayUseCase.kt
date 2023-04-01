package com.longkd.cinema.search.domain.usecase

import com.longkd.cinema.search.domain.SearchRepository

class GetMovieOfTheDayUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() = searchRepository.getMovieOfTheDay()
}
