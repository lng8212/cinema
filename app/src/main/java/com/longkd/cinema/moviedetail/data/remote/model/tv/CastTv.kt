package com.longkd.cinema.moviedetail.data.remote.model.tv

import com.longkd.cinema.moviedetail.domain.model.CastCrewPerson

data class CastTv(
    val adult: Boolean,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)

fun CastTv.mapToCastCrewPerson():CastCrewPerson{
    return CastCrewPerson(
        id = id,
        job = known_for_department,
        name = name,
        character = character,
        profile_path = profile_path
    )
}
