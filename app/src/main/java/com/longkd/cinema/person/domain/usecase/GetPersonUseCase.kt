package com.longkd.cinema.person.domain.usecase

import com.longkd.cinema.person.domain.PersonRepository

class GetPersonUseCase(private val personRepository: PersonRepository) {
    suspend operator fun invoke(personId: Int) = personRepository.getPerson(personId)
}