package com.base.app.data.repositories.city

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.APIService
import com.base.app.data.apis.RecommendApi
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 11/2/2023
 */
class CityRepository @Inject constructor(
    private val api: APIService,
    private val recommendApi: RecommendApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {
    suspend fun getAllLocation() =
        withContext(dispatcher) {
            when (val result =
                callApi { api.getAllCities() }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun searchForLocation(keyword: String) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.searchForCity(keyword) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun getRecommendCities(userId: String) =
        withContext(dispatcher) {
            when (val result =
                callApi { recommendApi.getRecommendLocation(userId) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }
}