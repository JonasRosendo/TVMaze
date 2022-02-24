package com.jonasrosendo.tvmaze.features.show_details.usecases

import com.jonasrosendo.data.repositories.ShowRepository
import com.jonasrosendo.model.episode.EpisodeResponse
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.functional.Either
import javax.inject.Inject

class GetShowEpisodes @Inject constructor(
    private val repository: ShowRepository
) {
    suspend operator fun invoke(showId: Int): Either<Failure, List<EpisodeResponse>> {

        val response = repository.getShowEpisodes(showId)
        val sortedEpisodes: List<EpisodeResponse>

        if (response is Either.Right) {
            sortedEpisodes = response.valueRight.sortedBy { it.number }
            response.right(sortedEpisodes)
        }

        return response
    }
}
