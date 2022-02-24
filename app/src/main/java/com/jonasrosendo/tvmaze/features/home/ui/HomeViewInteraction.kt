package com.jonasrosendo.tvmaze.features.home.ui

sealed class HomeViewInteraction {
    class SearchShowByName(val showName: String) : HomeViewInteraction()
    object GetShows : HomeViewInteraction()
}