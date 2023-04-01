package com.longkd.cinema.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.longkd.cinema.ui.model.MovieBigCardItem
import com.longkd.cinema.utils.list.BaseDiffUtil

class MovieBigCardItemAdapter(
    private val listener: MoviesBigCardAdapterListener
) : PagingDataAdapter<MovieBigCardItem, MovieBigCardItemViewHolder>(BaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieBigCardItemViewHolder {
        return MovieBigCardItemViewHolder.create(parent, movieClickItem)
    }

    override fun onBindViewHolder(holder: MovieBigCardItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    private val movieClickItem = MovieBigCardItemViewHolder.MovieBigCardItemClickListener { movieBigCardItem ->
        listener.onMovieClick(movieBigCardItem)
    }

    fun interface MoviesBigCardAdapterListener {
        fun onMovieClick(movieBigCardItem: MovieBigCardItem)
    }
}
