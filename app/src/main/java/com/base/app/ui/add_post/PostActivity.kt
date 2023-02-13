package com.base.app.ui.add_post

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityPostBinding
import com.base.app.ui.main.MainActivity
import com.esafirm.imagepicker.features.registerImagePicker

class PostActivity : BaseActivity<ActivityPostBinding>() {
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private val viewModel by viewModels<AddPostViewModel>()
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        handleImagePicker(imagesPicker[0].uri)
    }

    override fun getContentLayout(): Int = R.layout.activity_post

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@PostActivity)
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            imgUploadImage.setOnClickListener {
                imagePickerLauncher.launch(createConfig(true, 1))
            }
            btnUpload.setOnClickListener {
                viewModel.uploadImage(imageUri, imagePath, edtDescription.text.toString())
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            uploadImageResponse.observe(this@PostActivity) {
                if (it) {
                    showToast(this@PostActivity, resources.getString(R.string.str_success))
                    startActivity(Intent(this@PostActivity, MainActivity::class.java))
                    finishAffinity()
                } else {
                    showToast(this@PostActivity, resources.getString(R.string.error))
                    startActivity(Intent(this@PostActivity, MainActivity::class.java))
                    finishAffinity()
                }
            }
        }
    }

    private fun handleImagePicker(uri: Uri?) {
        binding.imgUploadImage.setImageURI(uri)
        imageUri = uri
        imagePath = uri?.let { getFileExtension(it) }
    }
}