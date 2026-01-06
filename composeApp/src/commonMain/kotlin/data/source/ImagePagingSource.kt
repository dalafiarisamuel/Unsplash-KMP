package data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import data.mapper.PhotoMapper
import data.remote.repository.ImageRepository
import data.remote.repository.Resource
import data.ui.model.Photo


internal class ImagePagingSource(
    private val repository: ImageRepository,
    private val photoMapper: PhotoMapper,
    private val query: String,
) : PagingSource<Int, Photo>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
        const val PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int {
        return STARTING_PAGE_INDEX
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return when (val response =
            repository.getImageSearchResult(query, position, params.loadSize)) {
            is Resource.Failure -> LoadResult.Error(response.error)
            is Resource.Success -> {
                val photos = photoMapper.mapToUIList(response.result.results)
                LoadResult.Page(
                    data = photos,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (position >= response.result.totalPages) null else position + 1
                )
            }
        }
    }
}
