package com.base.app.ui.group.add_group.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.base.app.R
import com.base.app.common.EventObserver
import com.base.app.data.models.group.request.CreateGroupRequest
import com.base.app.databinding.FragmentCreateGroupInformationBinding
import com.base.app.ui.group.add_group.CreateGroupInformationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateGroupInformationFragment : Fragment() {
    private var imageUri: Uri? = null
    private var imagePath: String? = null
    private val PICK_IMAGE = 20
    private val viewModel by viewModels<CreateGroupInformationViewModel>()
    private lateinit var binding: FragmentCreateGroupInformationBinding
    private val itemsValue = arrayOf("", "public", "private")
    private val items = arrayOf("Chọn quyền riêng tư", "Công khai", "Riêng tư")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateGroupInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textInputGroupName.setInfoEdt(null)
            textInputGroupDescription.setInfoEdt(null)
            val adapter =
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
            spinnerGroupPrivacy.adapter = adapter
            buttonUploadImage.setOnClickListener {
                chooseImageBanner()
            }
            buttonCreateGroup.setOnClickListener {
                if (textInputGroupName.getText().isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Bạn cần nhập tên nhóm", Toast.LENGTH_SHORT)
                        .show()
                } else if (textInputGroupDescription.getText().isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Bạn cần nhập mô tả của nhóm",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (spinnerGroupPrivacy.selectedItemPosition == 0) {
                    Toast.makeText(
                        requireContext(),
                        "Bạn cần chọn quyền riêng tư của nhóm",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.createGroupProgressBar.visibility = View.VISIBLE
                    viewModel.uploadImage(imageUri, imagePath)
                }
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        uploadImageToStorage.observe(viewLifecycleOwner, EventObserver {
            val request = CreateGroupRequest(
                binding.textInputGroupName.getText().toString(),
                binding.textInputGroupDescription.getText().toString(),
                it,
                Calendar.getInstance().time.time.toString(),
                viewModel.firebaseUser?.uid.toString(),
                itemsValue[binding.spinnerGroupPrivacy.selectedItemPosition]
            )
            viewModel.createGroup(request)
        })
        createGroupResponse.observe(viewLifecycleOwner, EventObserver {
            if (it.data != null) {
                val action =
                    CreateGroupInformationFragmentDirections.actionCreateGroupInformationFragmentToInviteMemberFragment(
                        it.data.id.toString(),
                        "invite"
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), it.status?.message, Toast.LENGTH_SHORT).show()
            }
            binding.createGroupProgressBar.visibility = View.GONE
        })
    }

    private fun chooseImageBanner() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE || resultCode == AppCompatActivity.RESULT_OK) {
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

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val map = MimeTypeMap.getSingleton()
        return map.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}