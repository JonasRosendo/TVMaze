package com.jonasrosendo.data.services

import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.model.showsbyname.Show
import com.jonasrosendo.model.showsbyname.ShowByNameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeService {

    @GET("shows")
    suspend fun getShows(@Query("page") page : Int) : List<ShowResponse>

    @GET("search/shows")
    suspend fun getShowByName(@Query("q") showName: String) : List<ShowByNameResponse>

    @GET("shows/{id}")
    suspend fun getShowById(@Path("id") id: Int) : Show

    @GET("shows/{id}/episodes")
    suspend fun getShowEpisodes(@Path("id") showId: Int): List<EpisodeResponse>
}