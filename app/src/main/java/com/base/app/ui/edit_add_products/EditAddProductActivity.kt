//package com.base.app.ui.edit_add_products
//
//import android.graphics.Bitmap
//import android.graphics.Rect
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.text.Html
//import android.view.View
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.core.text.HtmlCompat
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.base.app.R
//import com.base.app.base.activities.BaseActivity
//import com.base.app.common.*
//import com.base.app.common.CommonUtils.compressedImage
//import com.base.app.common.CommonUtils.getRandomString
//import com.base.app.common.CommonUtils.hideSoftKeyboard
//import com.base.app.data.models.response.product.Category
//import com.base.app.data.models.response.product.Data
//import com.base.app.data.models.response.product.ProductCategoryResponse
//import com.base.app.databinding.ActivityEditAddProductBinding
//import com.base.app.ui.custom.InputEdittextCustom
//import com.base.app.ui.edit_add_products.adapter.ProductCategoryAdapter
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.CustomTarget
//import com.bumptech.glide.request.transition.Transition
//import com.bytepay.app.ui.edit_add_products.EditAddProductViewModel
//import com.esafirm.imagepicker.features.registerImagePicker
//import dagger.hilt.android.AndroidEntryPoint
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import java.io.File
//import java.io.FileOutputStream
//import java.io.OutputStream
//import java.util.*
//
//
//@AndroidEntryPoint
//class EditAddProductActivity :
//    BaseActivity<ActivityEditAddProductBinding>(),
//    EditAddImageAdapter.IRefundImageCallBack, CommonUtils.KeyboardVisibilityEventListener,
//    InputEdittextCustom.TextChangeCallBack {
//
//    companion object {
//        const val IMAGE_LIMIT = 20
//    }
//
//    private val viewModel by viewModels<EditAddProductViewModel>()
//    private var keyboardHeight = 0
//    private var isSelect = false
//    lateinit var dataImage: MutableList<Any?>
//    private lateinit var imageAdapter: EditAddImageAdapter
//    private lateinit var productCategoryAdapter: ProductCategoryAdapter
//    private lateinit var body: MultipartBody.Part
//    private lateinit var images: ArrayList<MultipartBody.Part>
//    private lateinit var requestFile: RequestBody
//    private val imagePickerLauncher = registerImagePicker {
//        imagesPicker.clear()
//        imagesPicker.addAll(it)
//        viewModel.handleImages(this, imagesPicker)
//    }
//
//    lateinit var requestName: RequestBody
//    lateinit var requestProductPrice: RequestBody
//    lateinit var requestNumber: RequestBody
//    lateinit var requestDes: RequestBody
//    lateinit var requestCategoryId: RequestBody
//    lateinit var requestCategoryData: RequestBody
//    var editProductData: Data? = null
//    var categoryResponse: ProductCategoryResponse? = null
//    var isFirstEdit = false
//    override fun getContentLayout(): Int {
//        return R.layout.activity_edit_add_product
//    }
//
//    override fun initView() {
//        //registerObserverLoadingEvent(viewModel, this@EditAddProductActivity)
//        CommonUtils.showCustomUI(this)
//        //setupUI(binding.root, this)
//        paddingStatusBar(binding.toolbar.layoutToolbar)
//        setLightIconStatusBar(true)
//        binding.toolbar.tvTitle.text = resources.getString(R.string.str_add_product)
//        binding.edtProductName.setInfoEdt(0)
//        binding.edtProductDescription.setInfoEdt(0)
//        binding.edtProductPrice.setInfoEdt(0)
//        binding.edtProductPrice.initInterface(this)
//        binding.edtNumber.setInfoEdt(0)
//        binding.edtNumber.initInterface(this)
//        //rcv images
//        dataImage = ArrayList()
//        dataImage.add(EMPTY_STRING)
//        imageAdapter = EditAddImageAdapter(dataImage, this)
//        binding.rcvImages.layoutManager =
//            LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
//        binding.rcvImages.adapter = imageAdapter
//        binding.rcvImages.setItemViewCacheSize(IMAGE_LIMIT)
//        binding.tvHintSpinner.text = Html.fromHtml(
//            getString(R.string.str_product_category),
//            HtmlCompat.FROM_HTML_MODE_LEGACY
//        )
////        viewModel.getProductCategory(
////            OrderType.ASC.value,
////            DEFAULT_PAGE,
////            DEFAULT_TAKE_VALUE,
////            null,
////            null,
////            true,
////            null
////        )
////        if (intent?.getSerializableExtra(EXTRA_IS_EDIT_DATA) != null) {
////            isFirstEdit = true
////            editProductData = intent?.getSerializableExtra(EXTRA_IS_EDIT_DATA) as Data
////            viewModel.getProductDetails(editProductData!!.id)
////            binding.tvAdd.text = getString(R.string.str_update)
////            binding.toolbar.tvTitle.text = getString(R.string.str_edit_product)
////        }
//    }
//
//    override fun initListener() {
//        initKeyBoardPositionListener()
//        CommonUtils.setEventListener(this, this, window)
//        binding.toolbar.imgBack.setOnClickListener {
//            if (!isDoubleClick()) {
//                hideSoftKeyboard()
//                finish()
//            }
//        }
//        binding.lnlChooseImage.setOnClickListener {
//            if (!isDoubleClick()) {
//                imagePickerLauncher.launch(createConfig(false, 6))
//            }
//        }
//        binding.tvCancel.setOnClickListener {
//            if (!isDoubleClick()) {
//                finish()
//            }
//        }
////        binding.spinnerProductCategory.onItemSelectedListener = object :
////            AdapterView.OnItemSelectedListener {
////            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
////                binding.layoutProductCategoryDetails.removeAllViews()
////                var categoryData: JSONObject? = null
////                var categoryList: JSONObject? = null
////                var selectedCategory =
////                    binding.spinnerProductCategory.selectedItem as Category
////                if (selectedCategory.list != null) {
////                    categoryList = JSONObject(Gson().toJson(selectedCategory.list!!))
////                }
////                if (editProductData == null || !isFirstEdit) {
////                    if (categoryList != null) {
////                        for (keyStr in categoryList.keys()) {
////                            var keyValue = categoryList.get(keyStr) as String
////                            var resourceParser =
////                                resources.getLayout(R.layout.layout_product_category_custom)
////                            var edt =
////                                ProductCategoryCustom(
////                                    this@EditAddProductActivity,
////                                    resourceParser,
////                                    0
////                                )
////                            edt.setHintText(keyValue.replaceFirstChar {
////                                if (it.isLowerCase()) it.titlecase(
////                                    Locale.getDefault()
////                                ) else it.toString()
////                            })
////                            edt.tag = keyStr
////                            binding.layoutProductCategoryDetails.addView(edt)
////                        }
////                    }
////                } else {
////                    if (editProductData != null && editProductData!!.categoryData != null) {
////                        isFirstEdit = false
////                        categoryData = JSONObject(Gson().toJson(editProductData!!.categoryData))
////                        if (categoryList != null) {
////                            for (keyStr in categoryList.keys()) {
////                                var keyValue = categoryList.get(keyStr) as String
////                                var resourceParser =
////                                    resources.getLayout(R.layout.layout_product_category_custom)
////                                var edt =
////                                    ProductCategoryCustom(
////                                        this@EditAddProductActivity,
////                                        resourceParser,
////                                        0
////                                    )
////                                var keyData = EMPTY_STRING
////                                try {
////                                    keyData = categoryData?.get(keyStr) as String
////                                } catch (e: Exception) {
////                                    e.printStackTrace()
////                                }
////                                if (keyData.isEmpty()) edt.setHintText(keyValue.replaceFirstChar {
////                                    if (it.isLowerCase()) it.titlecase(
////                                        Locale.getDefault()
////                                    ) else it.toString()
////                                })
////                                else edt.setText(keyData,
////                                    keyValue.replaceFirstChar {
////                                        if (it.isLowerCase()) it.titlecase(
////                                            Locale.getDefault()
////                                        ) else it.toString()
////                                    })
////                                edt.tag = keyStr
////                                binding.layoutProductCategoryDetails.addView(edt)
////                            }
////                        }
////                    }
////                }
////            }
////
////            override fun onNothingSelected(p0: AdapterView<*>?) {
////            }
////        }
//
//        binding.tvAdd.setOnClickListener {
//            if (!isDoubleClick()) {
//                when {
//                    binding.edtProductName.getText()?.trim()?.isEmpty() == true -> {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.str_please_enter_product_name),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//                    binding.edtProductPrice.getText()?.toString()?.replace(".", "")?.trim()
//                        ?.isEmpty() == true -> {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.str_please_enter_product_price),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//                    binding.edtNumber.getText()?.toString()?.replace(".", "")?.trim()
//                        ?.isEmpty() == true -> {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.str_please_enter_warehouse),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//                    dataImage.size - 1 <= 0 -> {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.str_please_choose_photo),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//                    binding.edtNumber.getText()?.toString()?.replace(".", "")?.trim().toString()
//                        .toInt() <= 0 -> {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.str_please_min_number),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//                    else -> {
//                        requestName =
//                            binding.edtProductName.getText()?.trim().toString()
//                                .toRequestBody(MultipartBody.FORM)
//                        requestProductPrice =
//                            binding.edtProductPrice.getText()?.toString()?.replace(".", "")?.trim()
//                                .toString()
//                                .toRequestBody(MultipartBody.FORM)
//                        requestNumber =
//                            binding.edtNumber.getText()?.toString()?.replace(".", "")?.trim()
//                                .toString()
//                                .toRequestBody(MultipartBody.FORM)
//                        requestDes =
//                            binding.edtProductDescription.getText()?.trim().toString()
//                                .toRequestBody(MultipartBody.FORM)
//
//                        requestCategoryId =
//                            (binding.spinnerProductCategory.selectedItem as Category)?.id?.toRequestBody(
//                                MultipartBody.FORM
//                            )!!
//                        //here is my change(not the code is wrong)
////                        requestCategoryData = makeCategoryDataObject().toRequestBody(
////                            MultipartBody.FORM
////                        )
//
//                        if (editProductData != null) {
//                            images = ArrayList()
//                            if (dataImage.size > 2) {
//                                //viewModel.showIsLoading()
//                                Glide.with(this@EditAddProductActivity)
//                                    .asBitmap()
//                                    .load((dataImage[0] as Uri).toString())
//                                    .into(object : CustomTarget<Bitmap>() {
//                                        override fun onResourceReady(
//                                            resource: Bitmap,
//                                            transition: Transition<in Bitmap>?
//                                        ) {
//                                            var bitmap = resource
//                                            val f = persistImage(bitmap)
//                                            val requestFile = f
//                                                .asRequestBody("image/jpg".toMediaTypeOrNull())
//                                            val fileName = f.name
//                                            body = MultipartBody.Part.createFormData(
//                                                "imageMain",
//                                                fileName,
//                                                requestFile
//                                            )
//                                        }
//
//                                        override fun onLoadCleared(placeholder: Drawable?) {
//                                        }
//                                    })
//
//                                for (i in 1 until dataImage.size - 1) {
//                                    Glide.with(this@EditAddProductActivity)
//                                        .asBitmap()
//                                        .load((dataImage[i] as Uri).toString())
//                                        .into(object : CustomTarget<Bitmap>() {
//                                            override fun onResourceReady(
//                                                resource: Bitmap,
//                                                transition: Transition<in Bitmap>?
//                                            ) {
//                                                var bitmap = resource
//                                                val f = persistImage(bitmap)
//                                                val requestFile = f
//                                                    .asRequestBody("image/jpg".toMediaTypeOrNull())
//                                                val fileName = f.name
//                                                images.add(
//                                                    MultipartBody.Part.createFormData(
//                                                        "imageDescription",
//                                                        fileName,
//                                                        requestFile
//                                                    )
//                                                )
//                                                if (images.size >= dataImage.size - 2) {
//                                                    // viewModel.goneLoading()
//                                                }
//                                            }
//
//                                            override fun onLoadCleared(placeholder: Drawable?) {
//                                            }
//                                        })
//                                }
//                            } else {
//                                // viewModel.showIsLoading()
//                                Glide.with(this@EditAddProductActivity)
//                                    .asBitmap()
//                                    .load((dataImage[0] as Uri).toString())
//                                    .into(object : CustomTarget<Bitmap>() {
//                                        override fun onResourceReady(
//                                            resource: Bitmap,
//                                            transition: Transition<in Bitmap>?
//                                        ) {
//                                            var bitmap = resource
//                                            val f = persistImage(bitmap)
//                                            val requestFile = f
//                                                .asRequestBody("image/jpg".toMediaTypeOrNull())
//                                            val fileName = f.name
//                                            body = MultipartBody.Part.createFormData(
//                                                "imageMain",
//                                                fileName,
//                                                requestFile
//                                            )
//                                            //   viewModel.goneLoading()
//                                        }
//
//                                        override fun onLoadCleared(placeholder: Drawable?) {
//                                        }
//                                    })
//                            }
//                        } else {
//                            val file = File((dataImage[0] as Uri).toString())
//                            var length = file.length()
//                            length /= MB_LENGTH
//                            requestFile = if (length > LIMIT_SIZE) {
//                                compressedImage(applicationContext, file)
//                                    .asRequestBody("image/jpg".toMediaTypeOrNull())
//                            } else {
//                                file
//                                    .asRequestBody("image/jpg".toMediaTypeOrNull())
//                            }
//                            val fileName =
//                                getRandomString() + System.currentTimeMillis() + file.name
//
//                            body = MultipartBody.Part.createFormData(
//                                "imageMain",
//                                fileName,
//                                requestFile
//                            )
//                            if (dataImage.size > 2) {
//                                images = ArrayList()
//                                for (i in 1 until dataImage.size - 1) {
//                                    val f = File((dataImage[i] as Uri).toString())
//                                    var length = file.length()
//                                    length /= MB_LENGTH
//                                    requestFile = if (length > LIMIT_SIZE) {
//                                        compressedImage(applicationContext, f)
//                                            .asRequestBody("image/jpg".toMediaTypeOrNull())
//                                    } else {
//                                        f
//                                            .asRequestBody("image/jpg".toMediaTypeOrNull())
//                                    }
//                                    val fileName =
//                                        getRandomString() + System.currentTimeMillis() + f.name
//                                    images.add(
//                                        MultipartBody.Part.createFormData(
//                                            "imageDescription",
//                                            fileName,
//                                            requestFile
//                                        )
//                                    )
//                                }
////                                viewModel.addProduct(
////                                    body,
////                                    requestName,
////                                    requestProductPrice,
////                                    requestNumber,
////                                    requestDes, images,
////                                    requestCategoryId,
////                                    requestCategoryData
////                                )
//                            } else {
////                                viewModel.addProduct(
////                                    body,
////                                    requestName,
////                                    requestProductPrice,
////                                    requestNumber,
////                                    requestDes, null,
////                                    requestCategoryId,
////                                    requestCategoryData
////                                )
//                            }
//                        }
//                        hideSoftKeyboard()
//                    }
//                }
//            }
//        }
//
//    }
//
//    private fun initKeyBoardPositionListener() {
//        val rootView = window.decorView.findViewById<ConstraintLayout>(R.id.constraint_background)
//        rootView.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            window.decorView.getWindowVisibleDisplayFrame(r)
//            keyboardHeight =
//                kotlin.math.max(keyboardHeight, window.decorView.height - r.bottom - 70)
//        }
//    }
//
//    override fun observerLiveData() {
//        viewModel.apply {
//            uploadImagesResponse.observe(this@EditAddProductActivity, Observer { data ->
//                for (path in data)
//                    dataImage.add((dataImage.size - 1), Uri.parse(path))
//                imageAdapter.notifyDataSetChanged()
//                updateUIImage()
//            })
//
////            addProductResponse.observe(this@EditAddProductActivity, Observer {
////                Toast.makeText(
////                    this@EditAddProductActivity,
////                    getString(R.string.add_product_success),
////                    Toast.LENGTH_SHORT
////                )
////                    .show()
////
////                val intentUpdateListProduct = Intent()
////                intentUpdateListProduct.action = UPDATE_LIST_PRODUCT
////                sendBroadcast(intentUpdateListProduct)
////                finish()
////            })
////            editProductResponse.observe(this@EditAddProductActivity, Observer {
////                Toast.makeText(
////                    this@EditAddProductActivity,
////                    getString(R.string.update_product_success),
////                    Toast.LENGTH_SHORT
////                ).show()
////
////                val intentUpdateListProduct = Intent()
////                intentUpdateListProduct.action = UPDATE_LIST_PRODUCT
////                sendBroadcast(intentUpdateListProduct)
////                finish()
////            })
////            productDetailResponse.observe(this@EditAddProductActivity, Observer {
////                val mainPath = it.product?.image?.path
////                dataImage = ArrayList()
////                dataImage.add(Uri.parse(BuildConfig.API_URL_WEB + mainPath))
////                for (i in 0 until (it.imageAdd?.size ?: 0)) {
////                    if (it.imageAdd?.get(i)?.path != mainPath)
////                        dataImage.add(Uri.parse(BuildConfig.API_URL_WEB + it.imageAdd?.get(i)?.path))
////                }
////                dataImage.add(EMPTY_STRING)
////                imageAdapter = EditAddImageAdapter(dataImage, this@EditAddProductActivity)
////                binding.rcvImages.layoutManager =
////                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
////                binding.rcvImages.adapter = imageAdapter
////                updateUIImage()
////                binding.edtProductName.setText(it.product?.name?.trim() ?: EMPTY_STRING)
////                binding.edtProductPrice.setText(it.product?.price?.trim() ?: EMPTY_STRING)
////                binding.edtNumber.setText(it.product?.quantity?.toString() ?: EMPTY_STRING)
////                binding.edtProductDescription.setText(
////                    it.product?.description?.trim() ?: EMPTY_STRING
////                )
////            })
////
////            callApiEdit.observe(this@EditAddProductActivity, Observer {
////                Log.d("TAGGGGD", images.size.toString())
////                if (images.size > 0) {
////                    viewModel.editProduct(
////                        editProductData?.id ?: EMPTY_STRING,
////                        body,
////                        requestName,
////                        requestProductPrice,
////                        requestNumber,
////                        requestDes, images,
////                        requestCategoryId,
////                        requestCategoryData
////                    )
////                } else {
////                    viewModel.editProduct(
////                        editProductData?.id ?: EMPTY_STRING,
////                        body,
////                        requestName,
////                        requestProductPrice,
////                        requestNumber,
////                        requestDes, null,
////                        requestCategoryId,
////                        requestCategoryData
////                    )
////                }
////            })
////
////            productCategoryResponse.observe(this@EditAddProductActivity, Observer {
////                if (it != null) {
////                    categoryResponse = it
////                    makeCategoryView()
////                }
////            })
////        }
//
//        }
//
////    private fun makeCategoryDataObject(): String {
////        var data = JsonObject()
////        for (i in 0 until binding.layoutProductCategoryDetails.childCount) {
////            data.addProperty(
////                binding.layoutProductCategoryDetails.getChildAt(i).tag as String,
////                (binding.layoutProductCategoryDetails.getChildAt(i) as ProductCategoryCustom).getText()
////            )
////        }
////        return Gson().toJson(data)
////    }
////
////    private fun makeCategoryView() {
////        if (categoryResponse?.data != null) {
////            productCategoryAdapter = ProductCategoryAdapter(this, categoryResponse?.data!!)
////            binding.spinnerProductCategory.adapter = productCategoryAdapter
////            if (editProductData == null)
////                binding.spinnerProductCategory.setSelection(0)
////            else {
////                if (editProductData!!.category != null) {
////                    var selectedCategory: Category? = null
////                    for (i in 0 until categoryResponse?.data!!.size) {
////                        if (editProductData!!.category?.id == categoryResponse?.data!![i].id) {
////                            selectedCategory = categoryResponse?.data!![i]
////                            break
////                        }
////                    }
////                    Log.d(
////                        "TAG", "observerLiveData: $selectedCategory ${
////                            productCategoryAdapter.getPosition(
////                                selectedCategory
////                            )
////                        }"
////                    )
////                    if (selectedCategory != null) {
////                        binding.spinnerProductCategory.setSelection(
////                            productCategoryAdapter.getPosition(
////                                selectedCategory
////                            )
////                        )
////                    } else {
////                        binding.spinnerProductCategory.setSelection(0)
////                    }
////
////                }
////            }
////        }
//    }
//
//    override fun onDestroy() {
//        hideSoftKeyboard()
//        super.onDestroy()
//    }
//
//    override fun removeItem(position: Int, type: String) {
//        if (type == MANUAL_DELETE) {
//            if (!isDoubleClick()) {
//                dataImage.removeAt(position)
//                updateUIImage()
//            }
//        } else {
//            if (position >= 0) {
//                dataImage.removeAt(position)
//            }
//            updateUIImage()
//        }
//        imageAdapter.notifyDataSetChanged()
//    }
//
//    override fun onItemClick() {
//
//    }
//
//    override fun addImage() {
//        if (!isDoubleClick()) {
//            if (6 - (dataImage.size - 1) > 0) {
//                imagePickerLauncher.launch(createConfig(false, 6 - (dataImage.size - 1)))
//            } else {
//                Toast.makeText(
//                    this,
//                    getString(R.string.str_choose_maximum_6_image),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    private fun updateUIImage() {
//        if (imageAdapter.itemCount > 1) {
//            binding.rcvImages.visibility = View.VISIBLE
//            binding.lnlChooseImage.visibility = View.GONE
//        } else {
//            binding.rcvImages.visibility = View.GONE
//            binding.lnlChooseImage.visibility = View.VISIBLE
//        }
//    }
//
//    override fun onVisibilityChanged(isOpen: Boolean) {
//        val layoutParams = binding.constraintButtons.layoutParams as ConstraintLayout.LayoutParams
//        if (isOpen) {
//            layoutParams.bottomMargin = keyboardHeight
//        } else {
//            layoutParams.bottomMargin = 0
//        }
//        binding.constraintButtons.layoutParams = layoutParams
//    }
//
//
////    fun getBitmapFromURL(src: String?): Bitmap? {
////        return try {
////            val url = URL(src)
////            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
////            connection.setDoInput(true)
////            connection.connect()
////            val input: InputStream = connection.getInputStream()
////            BitmapFactory.decodeStream(input)
////        } catch (e: IOException) {
////            null
////        }
////    }
//
//    private fun persistImage(bitmap: Bitmap?): File {
//        val filesDir: File = filesDir
//        val imageFile = File(filesDir, getRandomString() + ".png")
//        val os: OutputStream
//        try {
//            os = FileOutputStream(imageFile)
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
//            os.flush()
//            os.close()
//        } catch (e: Exception) {
//
//        }
//        return imageFile
//    }
//
//    override fun getTaxCallBack(tax: Long) {
//    }
//
//}