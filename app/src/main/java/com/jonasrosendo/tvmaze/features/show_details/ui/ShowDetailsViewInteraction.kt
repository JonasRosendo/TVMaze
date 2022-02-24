package com.jonasrosendo.tvmaze.features.show_details.ui

sealed class ShowDetailsViewInteraction {
    data class RequestShowById(val id: Int) : ShowDetailsViewInteraction()
    data class GetShowById(val showId: Int) : ShowDetailsViewInteraction()
}