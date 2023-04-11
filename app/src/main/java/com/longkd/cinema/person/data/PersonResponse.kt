package com.longkd.cinema.person.data

data class PersonResponse(
    val id: Int,
    val birthday: String?,
    val known_for_department: String,
    val deathday: String?,
    val name: String,
    val gender: Int,
    val biography: String,
    val popularity: String,
    val place_of_birth: String?,
    val profile_path: String?,
    val adult: Boolean,
    val imdb_id: String,
    val homepage: String?
)

fun PersonResponse.mapToPerson(): Person {
    return Person(
        id,
        birthday,
        known_for_department,
        deathday,
        name,
        gender,
        biography,
        popularity,
        place_of_birth,
        profile_path,
        adult,
        imdb_id,
        homepage
    )
}
