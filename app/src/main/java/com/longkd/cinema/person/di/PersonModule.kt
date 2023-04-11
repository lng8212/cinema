package com.longkd.cinema.person.di

import android.content.SharedPreferences
import com.longkd.cinema.person.data.PersonApi
import com.longkd.cinema.person.data.PersonRepositoryImpl
import com.longkd.cinema.person.domain.PersonRepository
import com.longkd.cinema.person.domain.usecase.GetPersonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersonModule {
    @Provides
    @Singleton
    fun providePeopleApi(retrofit: Retrofit): PersonApi {
        return retrofit.create(PersonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePersonRepository(personApi: PersonApi, sharedPreferences: SharedPreferences): PersonRepository {
        return PersonRepositoryImpl(personApi, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetPersonUseCase(personRepository: PersonRepository) = GetPersonUseCase(personRepository)
}