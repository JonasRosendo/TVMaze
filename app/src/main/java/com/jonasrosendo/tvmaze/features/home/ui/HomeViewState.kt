package com.jonasrosendo.tvmaze.features.home.ui

import androidx.paging.PagingData
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.model.showsbyname.ShowByNameResponse
import com.jonasrosendo.shared_logic.exception.Failure
import kotlinx.coroutines.flow.Flow

sealed class HomeViewState {
    object Empty : HomeViewState()
    object Loading : HomeViewState()
    data class ShowRetrievedShowsByPage(val showResponse: PagingData<ShowResponse>) : HomeViewState()
    data class Error(val failure: Failure) : HomeViewState()
    data class ShowsByNameSuccess(val showResponse: List<ShowByNameResponse>) : HomeViewState()
    data class ShowsByNameError(val failure: Failure) : HomeViewState()
}