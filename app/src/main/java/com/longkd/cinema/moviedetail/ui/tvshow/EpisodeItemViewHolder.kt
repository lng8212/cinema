package com.longkd.cinema.moviedetail.ui.tvshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemEpisodeBinding
import com.longkd.cinema.moviedetail.ui.model.EpisodeItem
import com.bumptech.glide.Glide

class EpisodeItemViewHolder(private val binding: ItemEpisodeBinding) : ViewHolder(binding.root) {
    fun bind(episodeItem: EpisodeItem) {
        with(episodeItem) {
            val imageUrl = ImagesConfigData.secure_base_url + ImagesConfigData.still_sizes?.get(1) + stillPath

            binding.apply {
                episodeTextView.text = name
                Glide.with(root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(episodeImageView)
                runtimeTextView.text = root.context.getString(R.string.minutes, runtime.toString())
                overviewTextView.text = overview
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): EpisodeItemViewHolder {
            val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EpisodeItemViewHolder(binding)
        }
    }

}
