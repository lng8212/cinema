package com.longkd.cinema.moviedetail.ui.tvshow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.domain.usecase.CheckShowWishListedUseCase
import com.longkd.cinema.domain.usecase.InsertShowToDatabaseUseCase
import com.longkd.cinema.domain.usecase.RemoveShowFromDatabaseUseCase
import com.longkd.cinema.moviedetail.data.remote.model.tv.mapToEpisodeItem
import com.longkd.cinema.moviedetail.domain.model.mapToCastCrewItem
import com.longkd.cinema.moviedetail.domain.usecase.GetEpisodesUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetTvShowCastCrewListUseCase
import com.longkd.cinema.moviedetail.domain.usecase.GetTvShowDetailsUseCase
import com.longkd.cinema.moviedetail.ui.model.CastCrewItem
import com.longkd.cinema.moviedetail.ui.model.EpisodeItem
import com.longkd.cinema.moviedetail.ui.model.TvShowDetailState
import com.longkd.cinema.utils.Resource
import com.longkd.cinema.utils.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowDetailViewModel @Inject constructor(
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getTvShowCastCrewListUseCase: GetTvShowCastCrewListUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val insertShowToDatabaseUseCase: InsertShowToDatabaseUseCase,
    private val checkShowWishListedUseCase: CheckShowWishListedUseCase,
    private val removeShowFromDatabaseUseCase: RemoveShowFromDatabaseUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val showId = savedStateHandle.getOrThrow<Int>("showId")

    private var _showDetailState =
        MutableStateFlow(TvShowDetailState(isLoading = true, tvShowDetail = null))
    val showDetailState: StateFlow<TvShowDetailState>
        get() = _showDetailState

    private var _movieCastCrewList = MutableStateFlow<List<CastCrewItem>>(listOf())
    val movieCastCrewList: StateFlow<List<CastCrewItem>>
        get() = _movieCastCrewList

    private var _episodesState = MutableStateFlow<List<EpisodeItem>>(listOf())
    val episodesState: StateFlow<List<EpisodeItem>>
        get() = _episodesState

    var wishListedState = MutableStateFlow(false)

    init {
        checkShowWishListed()
    }

    fun getTvShowDetails() {
        viewModelScope.launch {
            getTvShowDetailsUseCase(showId).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { detail ->
                            _showDetailState.value =
                                _showDetailState.value.copy(tvShowDetail = detail, isLoading = false)
                        }
                    }
                    is Resource.Error -> {
                        _showDetailState.value =
                            _showDetailState.value.copy(tvShowDetail = null, isLoading = false)
                    }
                    is Resource.Loading -> {
                        _showDetailState.value = _showDetailState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun getShowCastCrew() {
        viewModelScope.launch {
            getTvShowCastCrewListUseCase(showId).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { list ->
                            _movieCastCrewList.value = list.map {
                                it.mapToCastCrewItem()
                            }
                        }
                    }
                    is Resource.Error -> {
                        //TODO error handle
                    }
                    is Resource.Loading -> {
                        //TODO loading handle
                    }
                }
            }
        }
    }

    fun getSeasonEpisodes(seasonNumber: Int) {
        viewModelScope.launch {
            getEpisodesUseCase(seasonNumber = seasonNumber, showId = showId).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { episodeList ->
                            _episodesState.value = episodeList.map {
                                it.mapToEpisodeItem()
                            }
                        }
                    }
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }
        }
    }

    fun insertShowToDatabase() {
        viewModelScope.launch {
            _showDetailState.value.tvShowDetail?.let { detail ->
                insertShowToDatabaseUseCase(detail)
                wishListedState.value = true
            }
        }
    }

    private fun checkShowWishListed() {
        viewModelScope.launch {
            wishListedState.value = checkShowWishListedUseCase(showId)
        }
    }

    fun removeShowFromDatabase() {
        viewModelScope.launch {
            removeShowFromDatabaseUseCase(showId)
            wishListedState.value = false
        }
    }
}
