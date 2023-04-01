package com.longkd.cinema.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.data.remote.pagingsource.PagingDataType
import com.longkd.cinema.domain.model.mapToMovieBigCardItem
import com.longkd.cinema.domain.usecase.GetMoviesPagerUseCase
import com.longkd.cinema.search.data.model.PersonListState
import com.longkd.cinema.search.domain.usecase.SearchPersonUseCase
import com.longkd.cinema.ui.model.MovieBigCardItem
import com.longkd.cinema.utils.Resource
import com.longkd.cinema.utils.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val searchPersonUseCase: SearchPersonUseCase,
    private val getMoviesPagerUseCase: GetMoviesPagerUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val firstQuery = savedStateHandle.getOrThrow<String>("searchQuery")

    private var _personListState = MutableStateFlow(PersonListState(isLoading = true, emptyList()))
    val personListState: StateFlow<PersonListState>
        get() = _personListState


    private val _moviesSearchState = MutableStateFlow<PagingData<MovieBigCardItem>>(PagingData.empty())
    val moviesSearchState: StateFlow<PagingData<MovieBigCardItem>>
        get() = _moviesSearchState


    init {
        searchPerson(firstQuery)
        searchMovie(firstQuery)
    }

    fun searchPerson(searchQuery: String) {
        viewModelScope.launch {
            searchPersonUseCase(searchQuery).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _personListState.value = PersonListState(isLoading = false, personItemList = it)
                        }
                    }
                    is Resource.Error -> {
                        //TODO error handling
                    }
                    is Resource.Loading -> {
                        _personListState.value = _personListState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    fun searchMovie(searchQuery: String) {
        viewModelScope.launch {
            getMoviesPagerUseCase(pagingDataType = PagingDataType.SearchMovies(searchQuery))
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { movieList ->
                    _moviesSearchState.value = movieList.map {
                        it.mapToMovieBigCardItem()
                    }
                }
        }
    }
}
