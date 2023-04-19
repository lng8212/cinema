package com.longkd.cinema.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemPersonBinding
import com.longkd.cinema.search.ui.model.PersonItem

class PersonItemViewHolder(private val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(personItem: PersonItem, onClickPerson: (Int) -> Unit) {
        with(binding) {
            with(personItem) {
                val imageUrl =
                    ImagesConfigData.secure_base_url + ImagesConfigData.profile_sizes?.get(1) + personItem.profile_path
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person_24)
                    .into(personImageView)

                nameTextView.text = name
                itemView.setOnClickListener {
                    onClickPerson.invoke(personItem.id)
                }
            }
        }
    }


    companion object {
        fun create(parent: ViewGroup): PersonItemViewHolder {
            return PersonItemViewHolder(
                ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
