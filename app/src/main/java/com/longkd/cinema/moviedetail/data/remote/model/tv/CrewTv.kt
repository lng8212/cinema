package com.longkd.cinema.moviedetail.data.remote.model.tv

import com.longkd.cinema.moviedetail.domain.model.CastCrewPerson

data class CrewTv(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int?,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)

fun CrewTv.mapToCastCrewPerson(): CastCrewPerson {
    return CastCrewPerson(
        id = id,
        job = job,
        name = name,
        character = null,
        profile_path = profile_path
    )
}
