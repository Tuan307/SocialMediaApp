package com.base.app.data.apis

import com.base.app.common.CONTENT_TYPE
import com.base.app.common.SERVER_KEY
import com.base.app.data.models.PushNotification
import com.base.app.data.models.request.*
import com.base.app.data.models.response.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("api/merchant/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: PushNotification): Response<ResponseBody>

}