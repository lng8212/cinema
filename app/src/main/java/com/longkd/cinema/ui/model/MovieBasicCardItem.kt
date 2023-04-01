package com.longkd.cinema.ui.model

import com.longkd.cinema.utils.list.RecyclerListItem

data class MovieBasicCardItem(
    val id: Int,
    val title: String,
    val rating: String,
    val genre: String,
    val imageUrl: String
) : RecyclerListItem {
    override fun areItemsTheSame(other: RecyclerListItem): Boolean {
        return other is MovieBasicCardItem && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerListItem): Boolean {
        return other is MovieBasicCardItem && other == this
    }

}
