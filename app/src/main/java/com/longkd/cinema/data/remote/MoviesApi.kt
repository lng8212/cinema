package com.longkd.cinema.data.remote

import com.longkd.cinema.data.remote.model.ConfigurationResponse
import com.longkd.cinema.data.remote.model.PopularMoviesResponse
import com.longkd.cinema.data.remote.model.TopRatedMoviesResponse
import com.longkd.cinema.data.remote.model.UpcomingMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<PopularMoviesResponse>

    @GET("configuration")
    suspend fun getConfigurationData(): Response<ConfigurationResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<UpcomingMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<TopRatedMoviesResponse>
}
