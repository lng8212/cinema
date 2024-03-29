package com.longkd.cinema.moviedetail.data.remote

import com.longkd.cinema.moviedetail.data.remote.model.movie.MovieCreditsResponse
import com.longkd.cinema.moviedetail.data.remote.model.movie.MovieDetailsResponse
import com.longkd.cinema.moviedetail.data.remote.model.movie.MovieTrailersResponse
import com.longkd.cinema.moviedetail.data.remote.model.tv.SeasonResponse
import com.longkd.cinema.moviedetail.data.remote.model.tv.TvShowCreditsResponse
import com.longkd.cinema.moviedetail.data.remote.model.tv.TvShowDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {

    @GET("movie/{movie_Id}")
    suspend fun getMovieDetails(
        @Path("movie_Id") movieId: Int,
        @Query("language") language: String
    ): Response<MovieDetailsResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): Response<MovieCreditsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailers(
        @Path("movie_id") movieId: Int
    ): Response<MovieTrailersResponse>

    @GET("tv/{show_id}")
    suspend fun getShowDetail(
        @Path("show_id") showId: Int,
        @Query("language") language: String
    ): Response<TvShowDetailResponse>

    @GET("tv/{show_id}/credits")
    suspend fun getTvShowCredits(
        @Path("show_id") showId: Int,
        @Query("language") language: String
    ): Response<TvShowCreditsResponse>

    @GET("tv/{show_id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("show_id") showId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String
    ): Response<SeasonResponse>

    @GET("tv/{show_id}/videos")
    suspend fun getTvShowTrailer(
        @Path("show_id") showId: Int
    ): Response<MovieTrailersResponse>
}
