package com.longkd.cinema.moviedetail.ui.tvshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.longkd.cinema.databinding.ItemSeasonBinding
import com.longkd.cinema.profile.settings.SeasonListItem

class SeasonItemViewHolder(
    private val binding: ItemSeasonBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(selectionListItem: SeasonListItem) {
        with(binding.selectionItemView) {
            text = selectionListItem.getVisibleName(itemView.context)
            isSelected = selectionListItem.isSelected
            textSize = if (isSelected) {
                24f
            } else {
                20f
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): SeasonItemViewHolder {
            val binding = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SeasonItemViewHolder(binding)
        }
    }
}
