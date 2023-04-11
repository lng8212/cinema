package com.longkd.cinema.person.ui.model

import com.longkd.cinema.person.data.Person

data class PersonState(
    val personDetail: Person? = null,
    val isLoading: Boolean = false
)