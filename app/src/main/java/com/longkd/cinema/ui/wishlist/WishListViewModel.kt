package com.longkd.cinema.ui.wishlist

import androidx.lifecycle.viewModelScope
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.domain.usecase.GetWishListedMediaUseCase
import com.longkd.cinema.ui.model.WishListCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val getWishListedMediaUseCase: GetWishListedMediaUseCase
) : BaseViewModel() {

    private var _wishListedMoviesState = MutableStateFlow<List<WishListCardItem>>(listOf())
    val wishListedMoviesState: StateFlow<List<WishListCardItem>>
        get() = _wishListedMoviesState

    fun getWishListedMovies() {
        viewModelScope.launch {
            getWishListedMediaUseCase().collectLatest {
                _wishListedMoviesState.value = it
            }
        }
    }
}
