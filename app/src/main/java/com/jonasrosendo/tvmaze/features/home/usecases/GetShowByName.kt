package com.jonasrosendo.tvmaze.features.home.usecases

import com.jonasrosendo.data.repositories.ShowRepository
import javax.inject.Inject

class GetShowByName @Inject constructor(
    private val repository: ShowRepository
) {
    suspend operator fun invoke(showName: String) = repository.getShowByName(showName)
}
