package com.longkd.cinema.moviedetail.di

import android.content.SharedPreferences
import com.longkd.cinema.moviedetail.data.MovieDetailRepositoryImpl
import com.longkd.cinema.moviedetail.data.remote.MovieDetailApi
import com.longkd.cinema.moviedetail.domain.MovieDetailRepository
import com.longkd.cinema.moviedetail.domain.usecase.GetEpisodesUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetMovieCastCrewListUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetMovieDetailsUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetMovieTrailerUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetShowTrailerUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetTvShowCastCrewListUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetTvShowDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {

    @Provides
    @Singleton
    fun provideMovieDetailApi(retrofit: Retrofit): MovieDetailApi {
        return retrofit.create(MovieDetailApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDetailRepository(
        movieDetailApi: MovieDetailApi,
        sharedPreferences: SharedPreferences
    ): MovieDetailRepository {
        return MovieDetailRepositoryImpl(
            movieDetailApi = movieDetailApi,
            sharedPreferences = sharedPreferences
        )
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetMovieDetailsUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetMovieCastCrewListUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetMovieCastCrewListUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetMovieTrailerUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetMovieTrailerUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetTvShowDetailsUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetTvShowDetailsUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetTvShowCastCrewListUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetTvShowCastCrewListUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetEpisodesUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetEpisodesUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun provideGetShowTrailerUseCase(movieDetailsRepository: MovieDetailRepository) =
        GetShowTrailerUseCase(movieDetailsRepository)
}
