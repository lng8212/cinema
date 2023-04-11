package com.longkd.cinema.moviedetail.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemCastCrewBinding
import com.longkd.cinema.moviedetail.ui.model.CastCrewItem

class CastCrewItemViewHolder(private val binding: ItemCastCrewBinding, private val onClickItem: (Int) -> Unit) :
    ViewHolder(binding.root) {

    fun bind(castCrewItem: CastCrewItem) {
        with(binding) {
            with(castCrewItem) {
                val imageUrl =
                    ImagesConfigData.secure_base_url + ImagesConfigData.profile_sizes?.get(1) + castCrewItem.profilePathUrl
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person_24)
                    .into(personImageView)

                nameTextView.text = name
                jobTextView.text = job
                characterTextView.text = character
            }
            root.setOnClickListener {
                onClickItem.invoke(castCrewItem.id)
            }
        }
    }


    companion object {
        fun create(parent: ViewGroup, onClickItem: (Int) -> Unit): CastCrewItemViewHolder {
            return CastCrewItemViewHolder(
                ItemCastCrewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem
            )
        }
    }
}
