package com.longkd.cinema.utils.list

interface RecyclerListItem {
    infix fun areItemsTheSame(other: RecyclerListItem): Boolean
    infix fun areContentsTheSame(other: RecyclerListItem): Boolean
}
