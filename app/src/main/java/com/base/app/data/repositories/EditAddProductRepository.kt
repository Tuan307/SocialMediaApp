//package com.base.app.data.repositories
//
//import com.base.app.base.network.BaseRemoteService
//import com.base.app.data.apis.Api
//import com.base.app.di.IoDispatcher
//import kotlinx.coroutines.CoroutineDispatcher
//import javax.inject.Inject
//import javax.inject.Named
//
//class EditAddProductRepository @Inject constructor(
//    private val bytePayAPI: Api,
//    @IoDispatcher private val dispatcher: CoroutineDispatcher
//) : BaseRemoteService() {
//
////    suspend fun addProduct(
////        file: MultipartBody.Part?,
////        requestName: RequestBody,
////        requestPrice: RequestBody,
////        requestNumber: RequestBody,
////        requestDes: RequestBody,
////        files: ArrayList<MultipartBody.Part>?,
////        categoryId: RequestBody,
////        categoryData: RequestBody
////    ) = withContext(dispatcher) {
////        when (val result = callApi {
////            bytePayWebAPI.addProduct(
////                file,
////                requestName,
////                requestPrice,
////                requestNumber,
////                requestDes,
////                files,
////                categoryId,
////                categoryData
////            )
////        }) {
////
////            is NetworkResult.Success -> {
////                result.data.let { it }
////            }
////            is NetworkResult.Error -> {
////                throw result.exception
////            }
////        }
////    }
////
////    suspend fun getProductDetails(id: String?) = withContext(dispatcher) {
////        when (val result = callApi { bytePayAPI.getProductDetails(id) }) {
////            is NetworkResult.Success -> {
////                result.data.let { it }
////            }
////            is NetworkResult.Error -> {
////                throw result.exception
////            }
////        }
////    }
////
////    suspend fun editProduct(
////        requestID: String,
////        file: MultipartBody.Part?,
////        requestName: RequestBody,
////        requestPrice: RequestBody,
////        requestNumber: RequestBody,
////        requestDes: RequestBody,
////        files: ArrayList<MultipartBody.Part>?,
////        categoryId: RequestBody,
////        categoryData: RequestBody
////    ) = withContext(dispatcher) {
////        when (val result = callApi {
////            bytePayWebAPI.editProduct(
////                requestID,
////                file,
////                requestName,
////                requestPrice,
////                requestNumber,
////                requestDes,
////                files,
////                categoryId,
////                categoryData
////            )
////        }) {
////            is NetworkResult.Success -> {
////                result.data.let { it }
////            }
////            is NetworkResult.Error -> {
////                throw result.exception
////            }
////        }
////    }
////
////    suspend fun getProductCategory(
////        order: String?,
////        page: Int?,
////        take: Int?,
////        keyword: String?,
////        listType: String?,
////        all: Boolean?,
////        status: String?
////    ) = withContext(dispatcher) {
////        when (val result = callApi {
////            bytePayAPI.getListProductCategory(
////                order,
////                page,
////                take,
////                keyword,
////                listType,
////                all,
////                status
////            )
////        }) {
////            is NetworkResult.Success -> {
////                result.data.let { it }
////            }
////            is NetworkResult.Error -> {
////                throw result.exception
////            }
////        }
////    }
//
//}