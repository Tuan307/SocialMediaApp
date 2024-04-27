package com.base.app.data.repositories.story

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.APIService
import com.base.app.data.models.request.AddStoryContentRequest
import com.base.app.data.models.request.AddStoryFolderRequest
import com.base.app.data.models.story.StoryContentModelResponse
import com.base.app.data.models.story.StoryFolderResponse
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 4/25/2024
 */
class StoryRepository @Inject constructor(
    private val api: APIService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {


    val getAllStoryFolder: Flow<StoryFolderResponse> = flow {
        val result = api.getAllStoryFolder()
        emit(result)
    }.catch {
        throw it.cause ?: it
    }.flowOn(dispatcher)

    fun getAllStoryContentFolder(id: Int): Flow<StoryContentModelResponse> = flow {
        val result = api.getStoryContentFolder(id)
        emit(result)
    }.catch {
        throw it.cause ?: it
    }.flowOn(dispatcher)

    suspend fun saveStoryFolder(request: AddStoryFolderRequest) = withContext(dispatcher) {
        when (val result = callApi { api.addStoryFolder(request) }) {
            is NetworkResult.Success -> {
                result.data
            }

            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun saveStoryContentFolder(request: AddStoryContentRequest) = withContext(dispatcher) {
        when (val result = callApi { api.addStoryContentFolder(request) }) {
            is NetworkResult.Success -> {
                result.data
            }

            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun deleteStoryFolder(id: Int) = withContext(dispatcher) {
        when (val result = callApi { api.deleteStoryFolder(id) }) {
            is NetworkResult.Success -> {
                result.data
            }

            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }
}