package com.base.app.data.repositories.notification

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.DatingAPI
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/4/2023
 */
class NotificationRepository @Inject constructor(
    private val api: DatingAPI,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {

    suspend fun getAllNotification(ownerId: String, pageCount: Int, pageNumber: Int) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.getAllNotification(ownerId, pageCount, pageNumber) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }

    suspend fun addNotification(request: AddNotificationRequest) =
        withContext(dispatcher) {
            when (val result =
                callApi { api.addNotification(request) }) {
                is NetworkResult.Success -> {
                    result.data
                }
                is NetworkResult.Error -> {
                    throw result.exception
                }
            }
        }
}
