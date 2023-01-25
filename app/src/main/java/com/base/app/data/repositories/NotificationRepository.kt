/*
package com.base.app.data.repositories

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.Api
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class NotificationRepository @Inject constructor(
    private val api: Api,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseRemoteService() {

//    suspend fun getListNotification(page: Int, unRead: String?, type: String?) =
//        withContext(dispatcher) {
//            when (val result = callApi {
//                api.getListNotification(page = page, unRead = unRead, type = type)
//            }) {
//                is NetworkResult.Success -> {
//                    result.data.let { it }
//                }
//                is NetworkResult.Error -> {
//                    throw result.exception
//                }
//            }
//        }
//
//    suspend fun getNumberUnreadNotification() =
//        withContext(dispatcher) {
//            when (val result = callApi {
//                api.getNumberUnreadNotification()
//            }) {
//                is NetworkResult.Success -> {
//                    result.data.let { it }
//                }
//                is NetworkResult.Error -> {
//                    throw result.exception
//                }
//            }
//        }
//
//    suspend fun deleteNotification(id: String?) =
//        withContext(dispatcher) {
//            when (val result = callApi {
//                api.deleteNotification(id)
//            }) {
//                is NetworkResult.Success -> {
//                    result.data.let { it }
//                }
//                is NetworkResult.Error -> {
//                    throw result.exception
//                }
//            }
//        }
//
//    suspend fun readNotification(id: String?) = withContext(dispatcher)
//    {
//        when (val result = callApi {
//            api.readNotification(id)
//        }) {
//            is NetworkResult.Success -> {
//                result.data.let { it }
//            }
//            is NetworkResult.Error -> {
//                throw result.exception
//            }
//        }
//    }
}*/
