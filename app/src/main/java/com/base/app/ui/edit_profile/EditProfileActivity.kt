package com.base.app.ui.edit_profile

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.registerImagePicker

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private val viewModel by viewModels<EditProfileViewModel>()
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        handleImagePicker(imagesPicker[0].uri)
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun initView() {
        viewModel.getInformation()
    }

    override fun initListener() {
        binding.apply {
            imgClose.setOnClickListener {
                finish()
            }
            imgAvatar.setOnClickListener {
                imagePickerLauncher.launch(createConfig(true, 1))
            }
            imgDoneEdit.setOnClickListener {
                viewModel.updateProfile(
                    fullName = edtEditName.getText().toString(),
                    userName = edtEditUserName.getText().toString(),
                    bio = edtEditBio.getText().toString(),
                    imageUri = imageUri,
                    imagePath = imagePath
                )
            }
        }
    }

    override fun observerLiveData() {
        viewModel.getUserInformation.observe(this@EditProfileActivity) {
            if (it != null) {
                binding.apply {
                    it.username?.let { it1 -> edtEditUserName.setText(it1) }
                    it.bio?.let { it1 -> edtEditBio.setText(it1) }
                    it.fullname?.let { it1 -> edtEditName.setText(it1) }
                    it.imageurl?.let { it1 ->
                        Glide.with(this@EditProfileActivity).load(it1).into(imgAvatar)
                    }
                }
            }
        }
        viewModel.getUpdateProfileResponse.observe(this@EditProfileActivity) {
            if (it) {
                showToast(this@EditProfileActivity, resources.getString(R.string.str_success))
                finish()
            } else {
                showToast(this@EditProfileActivity, resources.getString(R.string.error))
            }
        }
        viewModel.uploadImageResponse.observe(this@EditProfileActivity) {
            if (!it) {
                showToast(
                    this@EditProfileActivity,
                    "${resources.getString(R.string.error)} to upload your avatar"
                )
            }
        }
    }

    private fun handleImagePicker(uri: Uri?) {
        binding.imgAvatar.setImageURI(uri)
        imageUri = uri
        imagePath = uri?.let { getFileExtension(it) }
    }

}