package com.longkd.cinema.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemCarouselMovieBinding
import com.longkd.cinema.home.ui.model.CarouselMovieItem
import com.longkd.cinema.utils.dateFormatter
import com.bumptech.glide.Glide

class CarouselMovieItemViewHolder(private val binding: ItemCarouselMovieBinding) : ViewHolder(binding.root) {

    fun bind(carouselMovieItem: CarouselMovieItem) {
        with(binding) {
            with(carouselMovieItem) {
                val posterUrl = ImagesConfigData.secure_base_url + ImagesConfigData.backdrop_sizes?.get(1) + imageUrl

                val upcomingText = root.context.getString(R.string.on_date, dateFormatter(upcomingDate))
                titleTextView.text = title
                upcomingDateTextView.text = upcomingText
                Glide.with(binding.root.context)
                    .load(posterUrl)
                    .centerCrop()
                    .into(movieImageView)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CarouselMovieItemViewHolder {
            return CarouselMovieItemViewHolder(
                ItemCarouselMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
