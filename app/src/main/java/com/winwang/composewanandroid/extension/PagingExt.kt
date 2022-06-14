package com.winwang.composewanandroid.extension

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.winwang.composewanandroid.http.IResultBean
import com.winwang.composewanandroid.utils.AppLogUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.*


/**
 * Created by WinWang on 2022/6/14
 * Description->分页拓展
 */
fun <R : Any, I : Any> ViewModel.buildPager(
    config: AppPagingConfig = AppPagingConfig(),
    transformListBlock: (r: R?) -> List<I>?,
    callBlock: suspend (page: Int, config: Int) -> IResultBean<R>,
): Flow<PagingData<I>> {

    return pager(config, config.defaultPageIndex) {
        val currentPage = it.key ?: config.defaultPageIndex
        val result = callBlock.invoke(currentPage, if (currentPage == config.defaultPageIndex) config.initialLoadSize else config.pageSize)
        if (result.requestOk()) {
            var responseList = emptyList<I>()
            if (result.httpData() != null) {
                responseList = transformListBlock.invoke(result.httpData()) ?: emptyList()
                AppLogUtil.e("ssk2", "responseList.size=${responseList.size}")
            }

            val everyPageSize = config.pageSize
            val initPageSize = config.initialLoadSize
            val preKey = if (currentPage == config.defaultPageIndex) null else currentPage.minus(1)
            var nextKey: Int? = if (currentPage == config.defaultPageIndex) {
                (initPageSize / everyPageSize).plus(1)
            } else {
                currentPage.plus(1)
            }

//            if (responseList.size < everyPageSize || !config.enableLoadMore) {
//                nextKey = null
//            }

            PagingSource.LoadResult.Page(
                data = responseList,
                prevKey = preKey,
                nextKey = nextKey
            )
        } else {
            PagingSource.LoadResult.Error(PagingException(result.httpCode(), result.httpMsg()))
        }
    }
}

fun <R : Any, I : Any> ViewModel.buildStatefulItemPager(
    config: AppPagingConfig = AppPagingConfig(),
    transformListBlock: (r: R?) -> List<I>?,
    successBlock: (() -> Unit)? = null,
    errorBlock: (() -> Unit)? = null,
    callBlock: suspend (page: Int, pageSize: Int) -> IResultBean<R>
): Flow<PagingData<MutableState<I>>> {

    return pager(config, 1, errorBlock) {
        val currentPage = it.key ?: 1
        val result = callBlock.invoke(currentPage, if (currentPage == 1) config.initialLoadSize else config.pageSize)
        if (result.requestOk()) {
            val responseList = mutableListOf<MutableState<I>>()
            if (result.httpData() != null) {
                val transformList = transformListBlock.invoke(result.httpData()) ?: emptyList()
                transformList.forEach {
                    responseList.add(mutableStateOf(it))
                }
                AppLogUtil.e("ssk2", "responseList.size=${responseList.size}")
            }

            val everyPageSize = config.pageSize
            val initPageSize = config.initialLoadSize
            val preKey = if (currentPage == 1) null else currentPage.minus(1)
            var nextKey: Int? = if (currentPage == 1) {
                (initPageSize / everyPageSize).plus(1)
            } else {
                currentPage.plus(1)
            }

            if (responseList.size < everyPageSize || !config.enableLoadMore) {
                nextKey = null
            }

            successBlock?.invoke()
            PagingSource.LoadResult.Page(
                data = responseList,
                prevKey = preKey,
                nextKey = nextKey
            )
        } else {
            errorBlock?.invoke()
            PagingSource.LoadResult.Error(PagingException(result.httpCode(), result.httpMsg()))
        }
    }
}


fun <K : Any, V : Any> ViewModel.pager(
    config: AppPagingConfig = AppPagingConfig(),
    initialKey: K? = null,
    errorBlock: (() -> Unit)? = null,
    loadData: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>
): Flow<PagingData<V>> {
    val baseConfig = PagingConfig(
        config.pageSize,
        initialLoadSize = config.initialLoadSize,
        prefetchDistance = config.prefetchDistance,
        maxSize = config.maxSize,
        enablePlaceholders = config.enablePlaceholders
    )
    return Pager(
        config = baseConfig,
        initialKey = initialKey
    ) {
        object : PagingSource<K, V>() {
            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                val startRequestTime = Date().time
                return try {
                    val result = loadData.invoke(params)
                    val requestTimeCost = Date().time - startRequestTime
                    val delayTime = 0L.coerceAtLeast(config.minRequestCycle - requestTimeCost)
                    AppLogUtil.e("ssk2", "delayTime = $delayTime, requestTimeCost=$requestTimeCost")
                    delay(delayTime)
                    result
                } catch (e: Exception) {
                    e.printStackTrace()
                    val requestTimeCost = Date().time - startRequestTime
                    val delayTime = 0L.coerceAtLeast(config.minRequestCycle - requestTimeCost)
                    AppLogUtil.e("ssk2", "delayTime = $delayTime, requestTimeCost=$requestTimeCost")
                    delay(delayTime)
                    errorBlock?.invoke()
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<K, V>): K? {
                return initialKey
            }

        }
    }.flow.cachedIn(viewModelScope)
}

class PagingException(val errorCode: String, val errorMessage: String) : Exception("PagingException")
