package com.base.app.ui.edit_profile

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.user.User
import com.base.app.databinding.ActivityEditProfileBinding
import com.base.app.ui.splash.ChooseInterestActivity
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private val viewModel by viewModels<EditProfileViewModel>()
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private var currentUser: User? = null
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        if (imagesPicker.isNotEmpty()) {
            handleImagePicker(imagesPicker[0].uri)
        }
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@EditProfileActivity)
        viewModel.getInformation()
        binding.apply {
            edtEditBio.setInfoEdt(R.drawable.ic_edit)
            edtEditName.setInfoEdt(R.drawable.ic_account)
            edtEditUserName.setInfoEdt(R.drawable.ic_account)
        }
    }

    override fun initListener() {
        with(binding) {
            textUpdateUserInterest.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, ChooseInterestActivity::class.java)
                intent.putExtra("from", "update")
                startActivity(intent)
            }
            imgClose.setOnClickListener {
                finish()
            }
            imgAvatar.setOnClickListener {
                imagePickerLauncher.launch(createConfig(true, 1))
            }
            imgDoneEdit.setOnClickListener {
                currentUser?.userId?.let { it1 ->
                    viewModel.updateProfileRequest(
                        userId = it1,
                        fullName = edtEditName.getText().toString(),
                        userName = edtEditUserName.getText().toString(),
                        bio = edtEditBio.getText().toString(),
                        imageUri = imageUri,
                        imagePath = imagePath
                    )
                }
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
        getUserInformation.observe(this@EditProfileActivity) {
            currentUser = it
            if (it != null) {
                with(binding) {
                    it.userName?.let { it1 -> edtEditUserName.setText(it1) }
                    it.bio?.let { it1 -> edtEditBio.setText(it1) }
                    it.fullName?.let { it1 -> edtEditName.setText(it1) }
                    Glide.with(this@EditProfileActivity).load(it.imageUrl).into(imgAvatar)
                }
            }
        }
        updateProfileRemoteRequest.observe(this@EditProfileActivity) {
            updateProfile(it)
        }
        updateProfileRemote.observe(this@EditProfileActivity) {
            if (it.data != null) {
                val data = it.data
                updateProfileFireBase(
                    data.fullName.toString(),
                    data.userName.toString(),
                    data.bio.toString(),
                    data.imageUrl.toString()
                )
                showToast(this@EditProfileActivity, resources.getString(R.string.str_success))
                finish()
            } else {
                showToast(this@EditProfileActivity, it.status.message)
            }
        }
        uploadImageResponse.observe(this@EditProfileActivity) {
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