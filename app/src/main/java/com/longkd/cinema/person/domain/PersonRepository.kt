package com.longkd.cinema.person.domain

import android.provider.Contacts
import com.longkd.cinema.person.data.Person
import com.longkd.cinema.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getPerson(peopleId: Int): Flow<Resource<Person>>
}