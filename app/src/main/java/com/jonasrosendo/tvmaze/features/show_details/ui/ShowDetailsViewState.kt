package com.jonasrosendo.tvmaze.features.show_details.ui

import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.shared_logic.exception.Failure

sealed class ShowDetailsViewState {
    object Opened : ShowDetailsViewState()
    object Empty : ShowDetailsViewState()
    object Loading : ShowDetailsViewState()
    data class GetShowByIdSuccess(val show: Show, val episodes: List<EpisodeResponse>) : ShowDetailsViewState()
    data class GetShowByIdFailure(val failure: Failure) : ShowDetailsViewState()
}
