package com.jonasrosendo.data.repositories

import com.jonasrosendo.data.base.BaseRepository
import com.jonasrosendo.data.datasources.HomeDataSource
import javax.inject.Inject

class ShowRepository @Inject constructor(
    private val dataSource: HomeDataSource
) : BaseRepository() {

    suspend fun getShows() = safeApiCall { dataSource.getShows() }

    suspend fun getShowByName(showName: String) = safeApiCall { dataSource.getShowByName(showName) }

    suspend fun getShowById(showId: Int) = safeApiCall { dataSource.getShowById(showId) }

    suspend fun getShowEpisodes(showId: Int) = safeApiCall { dataSource.getShowEpisodes(showId) }
}