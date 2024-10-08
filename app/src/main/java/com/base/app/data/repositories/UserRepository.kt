package com.base.app.data.repositories

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.DatingAPI
import com.base.app.data.models.interest.request.AddUserInterestRequest
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
    private val api: DatingAPI,
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
}