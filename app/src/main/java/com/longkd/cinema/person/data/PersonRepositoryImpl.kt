package com.longkd.cinema.person.data

import android.content.SharedPreferences
import com.longkd.cinema.person.domain.PersonRepository
import com.longkd.cinema.utils.ENGLISH
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class PersonRepositoryImpl(private val personApi: PersonApi, private val sharedPreferences: SharedPreferences) :
    PersonRepository {
    override suspend fun getPerson(peopleId: Int): Flow<Resource<Person>> {
        val currentLanguage = sharedPreferences.getString("locale", ENGLISH) ?: ENGLISH
        return flow {
            emit(Resource.Loading(true))
            val response = try {
                personApi.getMovieDetails(peopleId, currentLanguage).body()
            } catch (e: IOException) {
                emit(Resource.Error(message = e.toString()))
                null
            } catch (e: HttpException) {
                emit(Resource.Error(message = e.toString()))
                null
            }
            response?.let {
                val person = it.mapToPerson()
                emit(Resource.Success(data = person))
            }
        }
    }
}