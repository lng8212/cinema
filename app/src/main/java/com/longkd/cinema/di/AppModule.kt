package com.longkd.cinema.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.longkd.cinema.BuildConfig
import com.longkd.cinema.auth.data.AuthRepositoryImpl
import com.longkd.cinema.auth.domain.AuthRepository
import com.longkd.cinema.data.MoviesRepositoryImpl
import com.longkd.cinema.data.local.MovieDatabase
import com.longkd.cinema.data.remote.MoviesApi
import com.longkd.cinema.domain.repository.MoviesRepository
import com.longkd.cinema.search.data.SearchApi
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val headerApiKey = BuildConfig.MOVIES_HEADER_API_KEY
        val headerInterceptor = Interceptor { chain ->
            chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $headerApiKey")
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .build()
                )
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit
            .create(MoviesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(
        moviesApi: MoviesApi,
        movieDatabase: MovieDatabase,
        searchApi: SearchApi,
        sharedPreferences: SharedPreferences
    ): MoviesRepository {
        return MoviesRepositoryImpl(
            moviesApi = moviesApi,
            movieDatabase = movieDatabase,
            searchApi = searchApi,
            sharedPreferences = sharedPreferences
        )
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFireBaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

}
