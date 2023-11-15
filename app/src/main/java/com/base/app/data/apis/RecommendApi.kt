package com.base.app.data.apis

import com.base.app.data.models.recommend.RecommendResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author tuanpham
 * @since 11/9/2023
 */
interface RecommendApi {
    @GET("travel/{userId}")
    suspend fun getRecommendLocation(@Path("userId") userId: String): Response<RecommendResponse>
}