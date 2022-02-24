package com.jonasrosendo.tvmaze.di

import com.jonasrosendo.data.repositories.ShowRepository
import com.jonasrosendo.tvmaze.features.home.usecases.GetShowByName
import com.jonasrosendo.tvmaze.features.home.usecases.GetShows
import com.jonasrosendo.tvmaze.features.show_details.usecases.GetShowById
import com.jonasrosendo.tvmaze.features.show_details.usecases.GetShowEpisodes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ShowModule {

    @Provides
    fun provideGetShows(repository: ShowRepository): GetShows = GetShows(repository)

    @Provides
    fun provideGetShowByName(repository: ShowRepository): GetShowByName = GetShowByName(repository)

    @Provides
    fun provideGetShowById(repository: ShowRepository): GetShowById = GetShowById(repository)

    @Provides
    fun provideGetShowEpisodes(repository: ShowRepository): GetShowEpisodes = GetShowEpisodes(repository)
}