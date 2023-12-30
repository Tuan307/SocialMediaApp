package com.base.app.data.repositories.profile

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.APIService
import com.base.app.data.models.request.UpdateProfileRequest
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 9/21/2023
 */

class UserProfileRepository @Inject constructor(
    private val api: APIService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {

    suspend fun getUserProfileImagePost(userId: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.getUserProfileNewsFeed(userId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getUserProfile(userId: String) =
        withContext(dispatcher) {
            when (val result = callApi { api.getUserProfile(userId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun updateUserProfile(request: UpdateProfileRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.updateUserProfile(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getAllSavedPost(userId: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result = callApi { api.getAllSavedPost(userId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }
}