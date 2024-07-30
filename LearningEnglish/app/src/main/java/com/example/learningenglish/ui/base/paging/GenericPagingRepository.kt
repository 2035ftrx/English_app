package com.example.learningenglish.ui.base.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow


abstract class AbstractGenericPagingRepository<T : Any> {

    fun getPagingData(): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GenericPagingSource { p, s -> loadPage(p, s) }
            }
        ).flow
    }

    abstract suspend fun loadPage(page: Int, size: Int): List<T>

}

class GenericPagingRepository<T : Any>(private val loadPage: suspend (page: Int, size: Int) -> List<T>) {
    fun getPagingData(): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { GenericPagingSource(loadPage) }
        ).flow
    }
}

class GenericPagingRepositorySource<T : Any>(private val pagingSource:()-> PagingSource<Int, T>) {
    fun getPagingData(): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { pagingSource() }
        ).flow
    }
}

private class GenericPagingSource<T : Any>(
    private val loadPage: suspend (page: Int, size: Int) -> List<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 0
            val data = loadPage(page, params.loadSize)
            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.isEmpty() || data.size < params.loadSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
