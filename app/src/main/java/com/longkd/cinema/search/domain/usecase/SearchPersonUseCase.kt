package com.longkd.cinema.search.domain.usecase

import com.longkd.cinema.search.domain.SearchRepository

class SearchPersonUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchQuery: String) = searchRepository.searchPerson(searchQuery)
}
