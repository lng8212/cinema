package com.longkd.cinema.moviedetail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemCastCrewBinding
import com.longkd.cinema.moviedetail.ui.model.CastCrewItem
import com.bumptech.glide.Glide

class CastCrewItemViewHolder(private val binding: ItemCastCrewBinding) : ViewHolder(binding.root) {

    fun bind(castCrewItem: CastCrewItem) {
        with(binding) {
            with(castCrewItem) {
                val imageUrl =
                    ImagesConfigData.secure_base_url + ImagesConfigData.profile_sizes?.get(0) + castCrewItem.profilePathUrl
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person_24)
                    .into(personImageView)

                nameTextView.text = name
                jobTextView.text = job
            }
        }
    }


    companion object {
        fun create(parent: ViewGroup): CastCrewItemViewHolder {
            return CastCrewItemViewHolder(
                ItemCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
