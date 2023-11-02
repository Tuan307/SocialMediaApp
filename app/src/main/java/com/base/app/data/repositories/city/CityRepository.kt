package com.base.app.data.repositories.city

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.DatingAPI
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 11/2/2023
 */
class CityRepository @Inject constructor(
    private val api: DatingAPI,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {
    suspend fun getAllCities() =
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
}