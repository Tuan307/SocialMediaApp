package com.base.app.data.repositories

import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.Api
import com.base.app.data.models.request.LoginRequest
import com.base.app.di.ApiMobile
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private  val bytePayAPI: Api,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseRemoteService() {

}