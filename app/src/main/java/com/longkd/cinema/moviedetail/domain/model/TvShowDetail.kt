package com.longkd.cinema.moviedetail.domain.model

import com.longkd.cinema.data.local.model.ShowEntity
import com.longkd.cinema.ui.model.WishListCardItem

data class TvShowDetail(
    val backdrop_path: String?,
    val episode_run_time: Int,
    val first_air_date: String,
    val genres: String,
    val id: Int,
    val name: String,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val original_name: String,
    val overview: String,
    val poster_path: String?,
    val vote_average: Double,
)

fun TvShowDetail.mapToShowEntity(): ShowEntity {
    return ShowEntity(
        id = id,
        backdrop_path = backdrop_path,
        episode_run_time = episode_run_time,
        first_air_date = first_air_date,
        genres = genres,
        name = name,
        number_of_episodes = number_of_episodes,
        number_of_seasons = number_of_seasons,
        original_name = original_name,
        overview = overview,
        poster_path = poster_path,
        vote_average = vote_average
    )
}

fun TvShowDetail.mapToWishListCardItem(): WishListCardItem {
    return WishListCardItem(
        backdrop_path = backdrop_path ?: "",
        genre = genres,
        id = id,
        title = name,
        vote_average = vote_average,
        isMovie = false
    )
}
