package com.longkd.cinema.moviedetail.ui.tvshow

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.longkd.cinema.moviedetail.ui.model.EpisodeItem
import com.longkd.cinema.utils.list.BaseDiffUtil

class EpisodesAdapter : ListAdapter<EpisodeItem, EpisodeItemViewHolder>(BaseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeItemViewHolder {
        return EpisodeItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EpisodeItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
