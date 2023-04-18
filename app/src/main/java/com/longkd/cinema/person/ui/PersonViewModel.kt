package com.longkd.cinema.person.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.person.domain.usecase.GetPersonUseCase
import com.longkd.cinema.person.ui.model.PersonState
import com.longkd.cinema.utils.Resource
import com.longkd.cinema.utils.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val getPersonUseCase: GetPersonUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val personId = savedStateHandle.getOrThrow<Int>("personId")
    private val _personState = MutableStateFlow(PersonState(personDetail = null, isLoading = true))

    init {
        getPerson(personId)
    }

    val personState: StateFlow<PersonState>
        get() = _personState


    private fun getPerson(personId: Int) {
        viewModelScope.launch {
            getPersonUseCase.invoke(personId).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            _personState.value = PersonState(isLoading = false, personDetail = it)
                        }
                    }
                    is Resource.Loading -> {
                        _personState.value = PersonState(isLoading = result.isLoading, personDetail = null)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

}