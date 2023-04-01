package com.longkd.cinema.search.domain

import com.longkd.cinema.domain.model.Movie
import com.longkd.cinema.search.ui.model.PersonItem
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchPerson(searchQuery: String): Flow<Resource<List<PersonItem>>>

    suspend fun getRecommendedMovies(movieId: Int): Flow<Resource<List<Movie>>>

    suspend fun getMovieOfTheDay(): Flow<Resource<Movie>>
}
