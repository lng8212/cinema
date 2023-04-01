package com.longkd.cinema.domain.di

import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.domain.usecase.CheckMovieWishListedUseCase
import com.longkd.cinema.domain.usecase.CheckShowWishListedUseCase
import com.longkd.cinema.domain.usecase.GetMoviesPagerUseCase
import com.longkd.cinema.domain.usecase.GetWishListedMediaUseCase
import com.longkd.cinema.domain.usecase.GetWishListedMoviesUseCase
import com.longkd.cinema.domain.usecase.InsertMovieToDatabaseUseCase
import com.longkd.cinema.domain.usecase.InsertShowToDatabaseUseCase
import com.longkd.cinema.domain.usecase.RemoveMovieFromDatabaseUseCase
import com.longkd.cinema.domain.usecase.RemoveShowFromDatabaseUseCase
import com.longkd.cinema.domain.usecase.SetImagesConfigDataUseCase
import com.longkd.cinema.home.usecase.GetCarouselMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMoviesPagerUseCase(moviesRepository: MoviesRepository): GetMoviesPagerUseCase {
        return GetMoviesPagerUseCase(moviesRepository)
    }

    @Provides
    @Singleton
    fun provideSetImagesConfigDataUseCase(moviesRepository: MoviesRepository) =
        SetImagesConfigDataUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideGetCarouselMoviesUseCase(moviesRepository: MoviesRepository) = GetCarouselMoviesUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideInsertMovieToDatabaseUseCase(moviesRepository: MoviesRepository) =
        InsertMovieToDatabaseUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideGetWishListedMoviesUseCase(moviesRepository: MoviesRepository) =
        GetWishListedMoviesUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideGetWishListedMediaUseCase(moviesRepository: MoviesRepository) =
        GetWishListedMediaUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideInsertShowToDatabaseUseCase(moviesRepository: MoviesRepository) =
        InsertShowToDatabaseUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideCheckMovieWishListedUseCase(moviesRepository: MoviesRepository) =
        CheckMovieWishListedUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideCheckShowWishListedUseCase(moviesRepository: MoviesRepository) =
        CheckShowWishListedUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideRemoveShowFromDatabaseUseCase(moviesRepository: MoviesRepository) =
        RemoveMovieFromDatabaseUseCase(moviesRepository)

    @Provides
    @Singleton
    fun provideRemoveMovieFromDatabaseUseCase(moviesRepository: MoviesRepository) =
        RemoveShowFromDatabaseUseCase(moviesRepository)
}
