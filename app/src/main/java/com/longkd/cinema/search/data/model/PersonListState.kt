package com.longkd.cinema.search.data.model

import com.longkd.cinema.search.ui.model.PersonItem

data class PersonListState(
    val isLoading: Boolean = false,
    val personItemList: List<PersonItem> = emptyList()
)
