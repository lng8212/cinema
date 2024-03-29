package com.longkd.cinema.data.remote.pagingsource

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.longkd.cinema.data.remote.MoviesApi
import com.longkd.cinema.data.remote.model.mapToMovie
import com.longkd.cinema.domain.model.Movie
import com.longkd.cinema.search.data.SearchApi
import com.longkd.cinema.search.data.model.mapToMovie
import com.longkd.cinema.utils.ENGLISH
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val moviesApi: MoviesApi,
    private val searchApi: SearchApi,
    private val pagingDataType: PagingDataType<Any>,
    private val sharedPreferences: SharedPreferences
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPage ->
                closestPage.prevKey?.plus(1) ?: closestPage.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentLanguage = sharedPreferences.getString("locale", ENGLISH) ?: ENGLISH
        val page = params.key ?: FIRST_PAGE_INDEX
        return try {
            val moviesApiResponse: List<Movie>? =
                when (pagingDataType) {
                    is PagingDataType.SearchMovies -> {
                        searchApi.searchMulti(
                            searchQuery = pagingDataType.parameter as String,
                            page = page,
                            language = currentLanguage
                        )
                            .body()?.results?.filter {
                                it.media_type == TYPE_TV || it.media_type == TYPE_MOVIE
                            }?.map {
                                it.mapToMovie()
                            }
                    }

                    is PagingDataType.PopularMovies -> {
                        moviesApi.getPopularMovies(page = page, language = currentLanguage).body()?.results?.map {
                            it.mapToMovie()
                        }
                    }
                    is PagingDataType.RecommendedMovies -> {
                        searchApi.getRecommendedMovies(
                            page = page,
                            movieId = pagingDataType.parameter as Int,
                            language = currentLanguage
                        )
                            .body()?.results?.map {
                                it.mapToMovie()
                            }
                    }
                    is PagingDataType.TopRatedMovies -> {
                        moviesApi.getTopRatedMovies(page = page, language = currentLanguage).body()?.results?.map {
                            it.mapToMovie()
                        }
                    }
                }

            val moviesList = moviesApiResponse.orEmpty()
            val nextKey = if (moviesList.isEmpty()) null else page.plus(1)
            val prevKey = if (page == FIRST_PAGE_INDEX) null else page.minus(1)
            LoadResult.Page(
                data = moviesList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val MOVIES_PAGE_SIZE = 10
        private const val FIRST_PAGE_INDEX = 1
        const val TYPE_TV = "tv"
        const val TYPE_MOVIE = "movie"
    }
}

sealed class PagingDataType<T>(val parameter: Any? = null) {
    class RecommendedMovies<Int>(movieId: Int) : PagingDataType<Int>(parameter = movieId)
    class SearchMovies<String>(searchQuery: String) : PagingDataType<String>(parameter = searchQuery)
    class PopularMovies() : PagingDataType<Any>()
    class TopRatedMovies() : PagingDataType<Any>()
}
