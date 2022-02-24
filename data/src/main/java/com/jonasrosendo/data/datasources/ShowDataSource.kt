package com.jonasrosendo.data.datasources

import androidx.paging.*
import com.jonasrosendo.data.base.BaseDataSource
import com.jonasrosendo.data.services.TVMazeService
import com.jonasrosendo.model.shows.ShowResponse
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.functional.Either
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class HomeDataSource(private val service: TVMazeService) : BaseDataSource() {
    suspend fun getShows(): Either<Failure, Flow<PagingData<ShowResponse>>> =
        managedExecution {
            Pager(PagingConfig(pageSize = 20, initialLoadSize = 20)) {
                PagedHomeDataSource(service)
            }.flow
        }

    suspend fun getShowByName(showName: String) = managedExecution {
        service.getShowByName(showName)
    }

    suspend fun getShowById(showId: Int) = managedExecution { service.getShowById(showId) }
    suspend fun getShowEpisodes(showId: Int) = managedExecution { service.getShowEpisodes(showId) }
}

private class PagedHomeDataSource(
    private val service: TVMazeService
) : PagingSource<Int, ShowResponse>() {

    override fun getRefreshKey(state: PagingState<Int, ShowResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShowResponse> {
        return try {
            val nextPageNumber = params.key ?: 1
            val pageSize: Int = params.loadSize
            val shows = service.getShows(nextPageNumber)

            val prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null

            val nextKey = if (shows.isNotEmpty()) nextPageNumber + 1 else null

            return LoadResult.Page(data = shows, prevKey, nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}

private sealed class Operation {
    object GetShows : Operation()
    data class GetShowByName(val showName: String) : Operation()
}