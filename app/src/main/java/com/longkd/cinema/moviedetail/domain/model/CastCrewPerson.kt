package com.longkd.cinema.moviedetail.domain.model

import com.longkd.cinema.moviedetail.ui.model.CastCrewItem

data class CastCrewPerson(
    val id: Int,
    val job: String,
    val name: String,
    val character: String?,
    val profile_path: String?
)

fun CastCrewPerson.mapToCastCrewItem(): CastCrewItem {
    return CastCrewItem(
        id = id,
        name = name,
        character = character,
        profilePathUrl = profile_path,
        job = job
    )
}
