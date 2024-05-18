package com.base.app.data.repositories.feed

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.APIService
import com.base.app.data.models.request.PostNewsFeedRequest
import com.base.app.data.models.request.SavedPostRequest
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 9/20/2023
 */
class NewsFeedRepository @Inject constructor(
    private val api: APIService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService(),NewsRepo {

    suspend fun getNewsFeed(pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.getNewsFeed(pageCount, pageNumber, "checkInTimestamp") }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getDetailPost(postId: String) = withContext(dispatcher) {
        when (val result = callApi { api.getDetailPost(postId) }) {
            is NetworkResult.Success -> {
                result.data.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun addNewsPost(request: PostNewsFeedRequest) = withContext(dispatcher) {
        when (val result = callApi { api.addNewsImagePost(request) }) {
            is NetworkResult.Success -> {
                result.data.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun deleteNewPost(postId: String) = withContext(dispatcher) {
        when (val result = callApi { api.deleteImagePost(postId) }) {
            is NetworkResult.Success -> {
                result.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun checkIfSavedPostExist(savedPostRequest: SavedPostRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.checkIfSavedPostExist(savedPostRequest) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun addSavedPost(savedPostRequest: SavedPostRequest) = withContext(dispatcher) {
        when (val result = callApi { api.addSavedPost(savedPostRequest) }) {
            is NetworkResult.Success -> {
                result.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }


}