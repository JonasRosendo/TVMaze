package com.jonasrosendo.tvmaze.features.show_details.ui

import androidx.lifecycle.viewModelScope
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.tvmaze.core.StateViewModel
import com.jonasrosendo.tvmaze.features.show_details.usecases.GetShowById
import com.jonasrosendo.tvmaze.features.show_details.usecases.GetShowEpisodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val getShowById: GetShowById,
    private val getShowEpisodes: GetShowEpisodes
) :
    StateViewModel<ShowDetailsViewState>(ShowDetailsViewState.Opened) {

    fun interact(interaction: ShowDetailsViewInteraction) {
        when (interaction) {
            is ShowDetailsViewInteraction.RequestShowById -> {}
            is ShowDetailsViewInteraction.GetShowById -> fetchShowsById(interaction.showId)
        }
    }

    private fun fetchShowsById(id: Int) {
        state.value = ShowDetailsViewState.Loading
        viewModelScope.launch {
            getShowById(id).fold(::onGetShowByIdFailure, ::onGetShowByIdSuccess)
        }
    }

    private fun onGetShowByIdSuccess(show: Show) {
        viewModelScope.launch {
            getShowEpisodes(showId = show.id).fold(::onGetShowWithEpisodesFailure) { onGetShowWithEpisodesSuccess(show, it) }
        }
    }

    private fun onGetShowWithEpisodesSuccess(show: Show, episodes: List<EpisodeResponse>) {
        state.value = ShowDetailsViewState.GetShowByIdSuccess(show, episodes)
    }

    private fun onGetShowWithEpisodesFailure(failure: Failure) {

    }

    private fun onGetShowByIdFailure(failure: Failure) {
        state.value = ShowDetailsViewState.GetShowByIdFailure(failure)
    }
}
