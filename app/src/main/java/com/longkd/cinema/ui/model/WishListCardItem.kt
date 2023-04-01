package com.longkd.cinema.ui.model

import com.longkd.cinema.utils.list.RecyclerListItem

data class WishListCardItem(
    val backdrop_path: String,
    val genre: String,
    val id: Int,
    val title: String,
    val vote_average: Double,
    val isMovie: Boolean
) : RecyclerListItem {
    override fun areItemsTheSame(other: RecyclerListItem): Boolean {
        return other is WishListCardItem && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerListItem): Boolean {
        return other is WishListCardItem && other == this
    }
}
