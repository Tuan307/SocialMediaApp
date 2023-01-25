package com.base.app.data.apis

import com.base.app.common.DEFAULT_TAKE_VALUE
import com.base.app.data.models.constants.OrderType
import com.base.app.data.models.request.*
import com.base.app.data.models.response.*
import com.base.app.data.models.response.calculateFee.CalculateFeeResponse
import com.base.app.data.models.response.list_bank.ListBankResponse
import com.base.app.data.models.response.login.User
import com.base.app.data.models.response.merchant.MerchantInfo
import com.base.app.data.models.response.notification.NotificationResponse
import com.base.app.data.models.response.support.MessageResponse
import com.base.app.data.models.response.support.SupportResponse
import com.bytepay.app.model.response.merchant_profile.MerchantProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("api/merchant/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/merchant/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("api/support")
    suspend fun sendSupportRequest(@Body request: CreateSupportRequest): Response<CreateSupportRequestResponse>

    @GET("/api/support/{id}")
    suspend fun getSupportMessage(
        @Path("id") id: String
    ): Response<MessageResponse>

    @GET("/api/support/merchant")
    fun getListSupport(
        @Query("order") order: String? = OrderType.ASC.value,
        @Query("page") page: Int,
        @Query("take") take: Int = DEFAULT_TAKE_VALUE
    ): Response<SupportResponse>

    @GET("api/shop/find-by-merchant/{merchantId}")
    fun getMerchantInfo(
        @Path("merchantId") merchantId: String
    ): Response<MerchantInfo>

    @Multipart
    @POST("api/shop")
    fun installPaymentLink(
        @Part file: MultipartBody.Part?,
        @Part("url") url: RequestBody?,
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("description") description: RequestBody,
    ): Response<MerchantInfo>

    @Multipart
    @PUT("api/shop/{id}")
    fun updatePaymentLink(
        @Path("id") id: String?,
        @Part file: MultipartBody.Part?,
        @Part("url") url: RequestBody?,
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("description") description: RequestBody,
    ): Response<MerchantInfo>

    @Multipart
    @POST("api/refund")
    fun submitRefundRequest(
        @Part("reasonId") reasonId: RequestBody,
        @Part("transactionId") transactionId: RequestBody,
        @Part("reasonOther") reasonOther: RequestBody,
        @Part("content") content: RequestBody,
        @Part files: List<MultipartBody.Part?>
    ): Response<Message>

    @GET("api/reason/merchant/list/refuse-refund")
    fun getReasons(): Response<ReasonsResponse>

    @GET("api/reason/merchant/list/refund")
    fun getReasonsRefund(): Response<ReasonsResponse>

    @GET("api/pin/detail-pin")
    fun pinDetail(): Response<Boolean>

    @POST("api/pin/check-pin")
    fun checkPin(@Body checkPinRequest: CheckPinRequest): Response<CheckPinResponse>

    @POST("api/pin")
    fun pinCodeSetting(@Body pinCodeSettingRequest: PinCodeSettingRequest): Response<PinCodeSettingResponse>

    @PUT("api/pin")
    fun changePin(@Body changPinRequest: ChangPinRequest): Response<CheckPinResponse>

    @PUT("api/pin/forgot-pin")
    fun forgotPinCode(@Body pinCodeSettingRequest: PinCodeSettingRequest): Response<Message>

    @POST("api/merchant/create-otp")
    fun sendOTPPinCode(@Body sendOTPPinCodeRequest: SendOTPPinCodeRequest): Response<SendOTPPinCodeResponse>

    @PUT("api/merchant/check-otp")
    fun checkOTP(@Body checkOTPRequest: CheckOTPRequest): Response<Message>

    @GET("api/merchant/profile")
    fun getMerchantProfile(): Response<MerchantProfileResponse>

    @Multipart
    @PUT("api/merchant/update-merchant-avatar")
    fun uploadAvatar(
        @Part file: MultipartBody.Part?
    ): Response<MerchantProfileResponse>

    @GET("api/payment/app/{id}")
    fun getDetailsTransaction(
        @Path("id") id: String
    ): Response<TransactionMerchantResponse.Data>

    @GET("api/notification/count")
    fun getNumberUnreadNotification(): Response<NumberUnreadNotificationResponse>

    @DELETE("api/notification/merchant/delete/{id}")
    fun deleteNotification(@Path("id") id: String?): Response<Message>

    @GET("api/notification/merchant/detail/{id}")
    fun readNotification(@Path("id") id: String?): Response<Message>

    @GET("api/notification/merchant/list")
    fun getListNotification(
        @Query("order") order: String? = OrderType.ASC.value,
        @Query("page") page: Int,
        @Query("take") take: Int = DEFAULT_TAKE_VALUE,
        @Query("unRead") unRead: String? = null,
        @Query("type") type: String? = null,
    ): Response<NotificationResponse>

    @GET("api/merchant-bank")
    fun getListBank(
        @Query("order") order: String? = OrderType.ASC.value,
        @Query("page") page: Int? = null,
        @Query("take") take: Int = DEFAULT_TAKE_VALUE
    ): Response<ListBankResponse>

    @POST("api/withdraw/caculate-fee")
    fun calculateFee(@Body calculateFeeRequest: CalculateFeeRequest): Response<CalculateFeeResponse>

    @POST("api/withdraw")
    fun withdraw(@Body withdrawRequest: WithdrawRequest): Response<WithdrawResponse>

    @GET("api/merchant/dashboard/statistic")
    fun getTotalTransaction(): Response<TotalTransactionResponse>
}