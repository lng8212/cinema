package com.longkd.cinema.moviedetail.domain

import com.longkd.cinema.moviedetail.data.remote.model.tv.Episode
import com.longkd.cinema.moviedetail.domain.model.CastCrewPerson
import com.longkd.cinema.moviedetail.domain.model.MovieDetail
import com.longkd.cinema.moviedetail.domain.model.Trailer
import com.longkd.cinema.moviedetail.domain.model.TvShowDetail
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {

    suspend fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetail>>

    suspend fun getMovieCastCrew(movieId: Int): Flow<Resource<List<CastCrewPerson>>>

    suspend fun getMovieTrailers(movieId: Int): Flow<Resource<List<Trailer>>>

    suspend fun getTvShowDetails(showId: Int): Flow<Resource<TvShowDetail>>

    suspend fun getTvShowCastCrew(showId: Int): Flow<Resource<List<CastCrewPerson>>>

    suspend fun getTvShowSeasonEpisodes(showId: Int, seasonNumber: Int): Flow<Resource<List<Episode>>>

    suspend fun getShowTrailers(showId: Int): Flow<Resource<List<Trailer>>>

}
