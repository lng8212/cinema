package com.longkd.cinema.person.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
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
) : Parcelable