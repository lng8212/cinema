package com.longkd.cinema.home.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.longkd.cinema.home.ui.model.CarouselMovieItem
import com.longkd.cinema.ui.MovieBasicCardItemViewHolder
import com.longkd.cinema.utils.list.BaseDiffUtil

class MoviesCarouselAdapter(private val listener: MoviesCarouselAdapterListener) :
    ListAdapter<CarouselMovieItem, CarouselMovieItemViewHolder>(BaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselMovieItemViewHolder {
        return CarouselMovieItemViewHolder.create(parent, movieClickItem)
    }

    override fun onBindViewHolder(holder: CarouselMovieItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private val movieClickItem = CarouselMovieItemViewHolder.MovieCardItemClickListener { carouselMovieItem->
        listener.onMovieClick(carouselMovieItem)
    }
    fun interface MoviesCarouselAdapterListener {
        fun onMovieClick(movieBasicCardItem: CarouselMovieItem)
    }
}
