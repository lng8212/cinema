package com.longkd.cinema.search.data

import android.content.SharedPreferences
import com.longkd.cinema.data.remote.model.mapToMovie
import com.longkd.cinema.domain.model.Movie
import com.longkd.cinema.search.data.model.mapToPersonItem
import com.longkd.cinema.search.domain.SearchRepository
import com.longkd.cinema.search.ui.model.PersonItem
import com.longkd.cinema.utils.ENGLISH
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchRepositoryImpl(
    private val searchApi: SearchApi,
    private val sharedPreferences: SharedPreferences
) : SearchRepository {


    override suspend fun searchPerson(searchQuery: String): Flow<Resource<List<PersonItem>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = try {
                searchApi.searchPerson(searchQuery).body()
            } catch (e: IOException) {
                emit(Resource.Error(message = e.toString()))
                null
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.toString()))
                null
            }
            response?.results?.let { personResultList ->
                val personItemList = personResultList.map {
                    it.mapToPersonItem()
                }
                emit(Resource.Success(data = personItemList))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun getRecommendedMovies(movieId: Int): Flow<Resource<List<Movie>>> {
        val currentLanguage = sharedPreferences.getString("locale", ENGLISH) ?: ENGLISH
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = try {
                searchApi.getRecommendedMovies(movieId = movieId, page = 1, language = currentLanguage).body()
            } catch (e: IOException) {
                emit(Resource.Error(message = e.toString()))
                null
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.toString()))
                null
            }
            response?.results?.let { movieResultList ->
                val movieList = movieResultList.map {
                    it.mapToMovie()
                }
                emit(Resource.Success(data = movieList))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun getMovieOfTheDay(): Flow<Resource<Movie>> {
        val currentLanguage = sharedPreferences.getString("locale", ENGLISH) ?: ENGLISH
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = try {
                searchApi.getNowPlayingMovies(language = currentLanguage).body()
            } catch (e: IOException) {
                emit(Resource.Error(message = e.toString()))
                null
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.toString()))
                null
            }
            response?.results?.let { movieResultList ->
                if (movieResultList.isNotEmpty()) {
                    val movie = movieResultList.random().mapToMovie()
                    emit(Resource.Success(data = movie))
                    emit(Resource.Loading(isLoading = false))
                }
            }
        }
    }
}
