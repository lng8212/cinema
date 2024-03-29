package com.longkd.cinema.ui.model

import com.longkd.cinema.utils.list.RecyclerListItem

data class MovieBigCardItem(
    val id: Int,
    val genre: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val mediaType: String?
) : RecyclerListItem {
    override fun areItemsTheSame(other: RecyclerListItem): Boolean {
        return other is MovieBigCardItem && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerListItem): Boolean {
        return other is MovieBigCardItem && other == this
    }
}
