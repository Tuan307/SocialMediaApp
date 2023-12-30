package com.base.app.data.repositories

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.APIService
import com.base.app.data.models.interest.request.AddUserInterestRequest
import com.base.app.data.models.request.FollowUserRequest
import com.base.app.data.models.request.RegisterRequest
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 9/18/2023
 */
class UserRepository @Inject constructor(
    private val api: APIService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {
    suspend fun registerUser(user: RegisterRequest) = withContext(dispatcher) {
        when (val result = callApi {
            api.registerUser(user)
        }) {
            is NetworkResult.Success -> {
                result.data
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }
    }

    suspend fun searchUsers(keyword: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result = callApi { api.searchUsers(keyword, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getAllNearbyUsers(userId: String, latitude: Double, longitude: Double, limit: Int) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.getAllNearbyUsers(userId, latitude, longitude, limit) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getAllInterests() =
        withContext(dispatcher) {
            when (val result = callApi { api.getAllInterests() }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getUpdateInterests(userId: String) =
        withContext(dispatcher) {
            when (val result = callApi { api.getUpdateInterests(userId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun checkIfUserHasInterests(userId: String) =
        withContext(dispatcher) {
            when (val result = callApi { api.checkIfUserHasInterests(userId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun saveUserInterest(request: AddUserInterestRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.saveUserInterest(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun updateUserInterest(request: AddUserInterestRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.updateUserInterest(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun followUser(request: FollowUserRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.followUser(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun unfollowUser(request: FollowUserRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.unfollowUser(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun isFollowingUser(request: FollowUserRequest) =
        withContext(dispatcher) {
            when (val result = callApi { api.isFollowingUser(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getFollowList(sourceId: String, type: String) =
        withContext(dispatcher) {
            when (val result = callApi { api.getFollowList(sourceId, type) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }
}