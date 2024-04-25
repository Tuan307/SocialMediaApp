package com.base.app.ui.story.create_story_folder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.base.app.R
import com.base.app.databinding.FragmentCreateStoryFolderBinding
import com.base.app.ui.main.fragment.profile.ProfileViewModel
import com.base.app.ui.utils.FileUtils.getFileExtension
import com.base.app.ui.utils.FileUtils.openImageFile
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStoryFolderFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCreateStoryFolderBinding
    private val viewModel by viewModels<CreateStoryFolderViewModel>()
    private val postActivityViewModel by activityViewModels<ProfileViewModel>()
    private var folderUri: Uri? = null
    private var folderPath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateStoryFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imageFolder.setOnClickListener {
                attachFilePermissionLauncher.launch(ATTACH_FILE_PERMISSION)
            }
            imageClose.setOnClickListener {
                dismiss()
            }
            buttonCreateFolder.setOnClickListener {
                val name = inputFolderName.text.toString()
                viewModel.uploadImageToCloud(folderUri, folderPath, name)
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        addStoryFolderResponse.observe(viewLifecycleOwner) {
            if (it.data != null) {
                postActivityViewModel.setAddStoryResponse(true)
                dismiss()
            } else {
                Toast.makeText(requireContext(), it.status?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val attachFilePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                attachFile()
            } else {
                onDeniedPermission()
            }
        }

    private fun onDeniedPermission() {
        Toast.makeText(requireContext(), "Can phai cap quyen", Toast.LENGTH_SHORT).show()
    }

    private fun attachFile() {
        attachFileResultLauncher.openImageFile("select files")
    }

    private val attachFileResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent = result.data ?: return@registerForActivityResult
                val uri: Uri?
                if (data.data != null) {
                    val selectedFile = data.data!!
                    uri = selectedFile
                    folderUri = uri
                    folderPath = requireActivity().getFileExtension(uri)
                    binding.imageFolder.setImageURI(uri)
                }
            }
        }

    companion object {
        const val TAG = "CreateStoryBottomDialog"
        fun newInstance(): CreateStoryFolderFragment {
            return CreateStoryFolderFragment()
        }

        private const val ATTACH_FILE_PERMISSION =
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
}