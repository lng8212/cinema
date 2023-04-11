package com.longkd.cinema.moviedetail.ui.model

import com.longkd.cinema.utils.list.RecyclerListItem

data class CastCrewItem(
    val id: Int,
    val name: String,
    val character: String?,
    val profilePathUrl: String?,
    val job: String
) : RecyclerListItem {
    override fun areItemsTheSame(other: RecyclerListItem): Boolean {
        return other is CastCrewItem && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerListItem): Boolean {
        return other is CastCrewItem && other == this
    }
}
