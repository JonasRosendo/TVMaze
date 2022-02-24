package com.jonasrosendo.tvmaze.features.show_details.usecases

import com.jonasrosendo.data.repositories.ShowRepository
import javax.inject.Inject

class GetShowById @Inject constructor(
    private val repository: ShowRepository
) {
    suspend operator fun invoke(showId: Int) =  repository.getShowById(showId)
}