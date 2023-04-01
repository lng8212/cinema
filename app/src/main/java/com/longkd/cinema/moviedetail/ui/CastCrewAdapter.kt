package com.longkd.cinema.moviedetail.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.longkd.cinema.moviedetail.ui.model.CastCrewItem
import com.longkd.cinema.utils.list.BaseDiffUtil

class CastCrewAdapter : ListAdapter<CastCrewItem, CastCrewItemViewHolder>(BaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastCrewItemViewHolder {
        return CastCrewItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CastCrewItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
