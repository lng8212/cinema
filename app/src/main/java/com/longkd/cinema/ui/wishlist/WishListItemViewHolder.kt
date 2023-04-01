package com.longkd.cinema.ui.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.longkd.cinema.ImagesConfigData
import com.longkd.cinema.R
import com.longkd.cinema.databinding.ItemWishlistBinding
import com.longkd.cinema.ui.model.WishListCardItem
import com.bumptech.glide.Glide

class WishListItemViewHolder(
    private val binding: ItemWishlistBinding,
    private val listener: WishListItemClickListener
) : ViewHolder(binding.root) {

    fun bind(wishListCardItem: WishListCardItem) {
        with(wishListCardItem) {
            val imageUrl = ImagesConfigData.secure_base_url + ImagesConfigData.backdrop_sizes?.get(1) + backdrop_path

            val typeText =
                if (isMovie) binding.root.context.getString(R.string.movie) else binding.root.context.getString(R.string.tv)
            with(binding) {
                root.setOnClickListener {
                    listener.onClick(wishListCardItem)
                }
                root.setOnLongClickListener {
                    listener.onLongClick(wishListCardItem)
                    true
                }
                genreTextView.text = genre
                movieNameTextView.text = title
                ratingTextView.text = vote_average.toString().substring(0, 3)
                typeTextView.text = typeText
                Glide.with(root.context).load(imageUrl).centerCrop().into(posterImageView)
            }
        }
    }

    interface WishListItemClickListener {
        fun onClick(wishListCardItem: WishListCardItem)
        fun onLongClick(wishListCardItem: WishListCardItem)
    }

    companion object {
        fun create(parent: ViewGroup, listener: WishListItemClickListener): WishListItemViewHolder {
            return WishListItemViewHolder(
                ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                listener
            )
        }
    }
}
