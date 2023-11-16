package com.base.app.ui.group.update_group

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.EventObserver
import com.base.app.data.models.group.request.UpdateGroupRequest
import com.base.app.databinding.ActivityUpdateGroupBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateGroupActivity : BaseActivity<ActivityUpdateGroupBinding>() {
    private val viewModel by viewModels<UpdateGroupViewModel>()
    private val itemsValue = arrayOf("", "public", "private")
    private val items = arrayOf("Chọn quyền riêng tư", "Công khai", "Riêng tư")
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private val PICK_IMAGE = 20

    override fun initView() = with(binding) {
        registerObserverErrorEvent(viewModel, this@UpdateGroupActivity)
        registerObserverLoadingEvent(viewModel, this@UpdateGroupActivity)
        val intent = intent
        viewModel.groupId = intent.getLongExtra("groupId", 0)
        if (viewModel.groupId != 0.toLong()) {
            viewModel.getGroupInformation(viewModel.groupId)
        }
        val adapter =
            ArrayAdapter(
                this@UpdateGroupActivity,
                R.layout.support_simple_spinner_dropdown_item,
                items
            )
        spinnerGroupPrivacy.adapter = adapter
        buttonUploadImage.setOnClickListener {
            chooseImageBanner()
        }
        buttonCreateGroup.setOnClickListener {
            if (textInputGroupName.getText().isNullOrEmpty()) {
                Toast.makeText(
                    this@UpdateGroupActivity,
                    "Bạn cần nhập tên nhóm",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (textInputGroupDescription.getText().isNullOrEmpty()) {
                Toast.makeText(
                    this@UpdateGroupActivity,
                    "Bạn cần nhập mô tả của nhóm",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (spinnerGroupPrivacy.selectedItemPosition == 0) {
                Toast.makeText(
                    this@UpdateGroupActivity,
                    "Bạn cần chọn quyền riêng tư của nhóm",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.uploadImage(imageUri, imagePath)
            }
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.activity_update_group
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        with(viewModel) {

            uploadImageToStorage.observe(this@UpdateGroupActivity, EventObserver {
                val request = UpdateGroupRequest(
                    binding.textInputGroupName.getText().toString(),
                    binding.textInputGroupDescription.getText().toString(),
                    it,
                    itemsValue[binding.spinnerGroupPrivacy.selectedItemPosition]
                )
                viewModel.updateGroup(viewModel.groupId, request)
            })

            updateGroupResponse.observe(this@UpdateGroupActivity, EventObserver {
                if (it == "Thành công") {
                    Toast.makeText(this@UpdateGroupActivity, it, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@UpdateGroupActivity, it, Toast.LENGTH_SHORT).show()
                }
            })

            informationResponse.observe(this@UpdateGroupActivity) {
                binding.textInputGroupName.setText(it.groupName)
                binding.textInputGroupDescription.setText(it.groupDescription)
                binding.imageChooseBanner.visibility = View.VISIBLE
                Glide.with(this@UpdateGroupActivity).load(it.groupImage)
                    .into(binding.imageChooseBanner)
                when (it.privacy) {
                    "private" -> {
                        binding.spinnerGroupPrivacy.setSelection(2)
                    }
                    "public" -> {
                        binding.spinnerGroupPrivacy.setSelection(1)
                    }
                    else -> {
                        binding.spinnerGroupPrivacy.setSelection(0)
                    }
                }
            }
        }
    }

    private fun chooseImageBanner() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE || resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                imagePath = data.data?.let { getFileExtension(it) }
                if (imageUri != null) {
                    binding.imageChooseBanner.visibility = View.VISIBLE
                    binding.imageChooseBanner.setImageURI(imageUri)
                }
            }
        }
    }


}