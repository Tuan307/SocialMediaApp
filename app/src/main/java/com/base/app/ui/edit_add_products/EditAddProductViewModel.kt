//package com.bytepay.app.ui.edit_add_products
//
////import com.baseapp.data.models.response.Message
////import com.base.app.data.models.response.product_detail.ProductDetailResponse
//import android.app.Activity
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.base.app.base.viewmodel.BaseViewModel
//import com.base.app.common.CommonUtils
//import com.base.app.data.repositories.EditAddProductRepository
//import com.esafirm.imagepicker.model.Image
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class EditAddProductViewModel @Inject constructor(private val editAddProductRepository: EditAddProductRepository) :
//    BaseViewModel() {
//
//    var uploadImagesResponse: MutableLiveData<List<String>> = MutableLiveData()
//
//    fun handleImages(
//        activity: Activity,
//        data: List<Image>
//    ) {
//        showLoading(true)
////        var isRun = true
////        val parts: ArrayList<String> = ArrayList()
////        parentJob = viewModelScope.launch(handler) {
////            if (isRun) {
////                isRun = false
////                for (i: Int in data.indices) {
////                    val file = CommonUtils.createRotatedFile(data[i].path, activity)
////                    parts.add(file.absolutePath)
////                }
////            }
////            uploadImagesResponse.postValue(parts)
////        }
//        registerJobFinish()
//    }
//
//    private var _createImageResponse: MutableLiveData<Bitmap> = MutableLiveData()
//    val createImageResponse: LiveData<Bitmap>
//        get() = _createImageResponse
//
//    fun createImage(
//        activity: Activity,
//        data: List<Image>
//    ) {
//        var isRun = true
//        var isFinish = true
//        var bitmap: Bitmap? = null
//        parentJob = viewModelScope.launch(handler) {
//            if (isRun) {
//                isRun = false
//                for (i: Int in data.indices) {
//                    val file = CommonUtils.createRotatedFile(data[i].path, activity)
//                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                }
//            }
//            if (isFinish) {
//                isFinish = false
//                _createImageResponse.postValue(bitmap!!)
//            }
//        }
//    }
//
////    private val _addProduct = MutableLiveData<Message>()
////    val addProductResponse: LiveData<Message>
////        get() = _addProduct
////
////    fun addProduct(
////        file: MultipartBody.Part?,
////        requestName: RequestBody,
////        requestPrice: RequestBody,
////        requestNumber: RequestBody,
////        requestDes: RequestBody,
////        files: ArrayList<MultipartBody.Part>?,
////        categoryId: RequestBody,
////        categoryData: RequestBody
////    ) {
////        showLoading(true)
////        parentJob = viewModelScope.launch(handler) {
////            val addProduct = editAddProductRepository.addProduct(
////                file,
////                requestName,
////                requestPrice,
////                requestNumber,
////                requestDes,
////                files,
////                categoryId,
////                categoryData
////            )
////            _addProduct.postValue(addProduct)
////        }
////        registerJobFinish()
////    }
////
////    private val _productDetailResponse = MutableLiveData<ProductDetailResponse>()
////    val productDetailResponse: LiveData<ProductDetailResponse>
////        get() = _productDetailResponse
////
////    fun getProductDetails(id: String?) {
////        showLoading(true)
////        parentJob = viewModelScope.launch(handler) {
////            val productDetail = editAddProductRepository.getProductDetails(id)
////            _productDetailResponse.postValue(productDetail)
////        }
////        registerJobFinish()
////    }
////
////    val callApiEdit = MutableLiveData<Boolean>()
////    fun goneLoading() {
////        registerJobFinish()
////        callApiEdit.postValue(true)
////    }
////
////    fun showIsLoading() {
////        showLoading(true)
////    }
////
////    private val _editProduct = MutableLiveData<Message>()
////    val editProductResponse: LiveData<Message>
////        get() = _editProduct
////
////    fun editProduct(
////        requestID: String,
////        file: MultipartBody.Part?,
////        requestName: RequestBody,
////        requestPrice: RequestBody,
////        requestNumber: RequestBody,
////        requestDes: RequestBody,
////        files: ArrayList<MultipartBody.Part>?,
////        categoryId: RequestBody,
////        categoryData: RequestBody
////    ) {
////        showLoading(true)
////        parentJob = viewModelScope.launch(handler) {
////            val editProduct = editAddProductRepository.editProduct(
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
////            _editProduct.postValue(editProduct)
////        }
////        registerJobFinish()
////    }
////
////    private val _productCategoryResponse = MutableLiveData<ProductCategoryResponse>()
////    val productCategoryResponse: LiveData<ProductCategoryResponse>
////        get() = _productCategoryResponse
////
////    fun getProductCategory(
////        order: String?,
////        page: Int?,
////        take: Int?,
////        keyword: String?,
////        listType: String?,
////        all: Boolean?,
////        status: String?
////    ) {
////        showLoading(true)
////        parentJob = viewModelScope.launch(handler) {
////            val productCategory = editAddProductRepository.getProductCategory(
////                order,
////                page,
////                take,
////                keyword,
////                listType,
////                all,
////                status
////            )
////            _productCategoryResponse.postValue(productCategory)
////        }
////        registerJobFinish()
////    }
//
//
//}
