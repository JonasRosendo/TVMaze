package com.jonasrosendo.tvmaze.features.home.ui

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.model.showsbyname.ShowByNameResponse
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.tvmaze.core.StateViewModel
import com.jonasrosendo.tvmaze.features.home.usecases.GetShowByName
import com.jonasrosendo.tvmaze.features.home.usecases.GetShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getShows: GetShows,
    private val getShowByName: GetShowByName
) : StateViewModel<HomeViewState>(HomeViewState.Empty) {

    fun interact(interaction: HomeViewInteraction) {
        when (interaction) {
            HomeViewInteraction.GetShows -> getShowsByPage()
            is HomeViewInteraction.SearchShowByName -> searchShowByName(interaction.showName)
        }
    }

    private fun searchShowByName(showName: String) {
        state.value = HomeViewState.Loading
        viewModelScope.launch {
            getShowByName(showName).fold(::onGetShowByNameFailure, ::onGetShowByNameSuccess)
        }
    }

    private fun onGetShowByNameSuccess(shows: List<ShowByNameResponse>) {
        state.value = HomeViewState.ShowsByNameSuccess(shows)
    }

    private fun onGetShowByNameFailure(failure: Failure) {

    }

    private fun getShowsByPage() {
        state.value = HomeViewState.Loading

        viewModelScope.launch {
            getShows().fold(::onGetShowsByPageFailure, ::onGetShowsByPageSuccess)
        }
    }

    private fun onGetShowsByPageSuccess(shows: Flow<PagingData<ShowResponse>>) {
        viewModelScope.launch {
            shows.cachedIn(this).collectLatest {
                state.value = HomeViewState.ShowRetrievedShowsByPage(it)
            }
        }
    }

    private fun onGetShowsByPageFailure(failure: Failure) {
        state.value = HomeViewState.Error(failure)
    }
}
