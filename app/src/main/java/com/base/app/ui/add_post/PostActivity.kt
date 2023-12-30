package com.base.app.ui.add_post

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.Utils
import com.base.app.data.models.request.PostNewsFeedRequest
import com.base.app.databinding.ActivityPostBinding
import com.base.app.ui.add_post.map_apdater.ShowImageAdapter
import com.base.app.ui.group.GroupActivity
import com.base.app.ui.main.MainActivity
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@AndroidEntryPoint
class PostActivity : BaseActivity<ActivityPostBinding>(), ShowImageAdapter.ShowImageClickListener {
    private var imageUri: ArrayList<Uri> = arrayListOf()
    private var imagePath: ArrayList<String> = arrayListOf()
    private val viewModel by viewModels<AddPostViewModel>()
    private val showImageList = ArrayList<Uri>()
    private var videoUri: Uri? = null
    private var videoPath: String? = null
    private val PICK_VIDEO = 10
    private var from = ""
    private var groupId = 0.toLong()
    private lateinit var showImageAdapter: ShowImageAdapter
    private val imagePickerLauncher = registerImagePicker {
        imagesPicker.clear()
        imagesPicker.addAll(it)
        handleImagePicker(imagesPicker)
    }

    override fun getContentLayout(): Int = R.layout.activity_post

    override fun initView() {
        val intent = intent
        from = intent.getStringExtra("from").toString()
        if (from == "group") {
            groupId = intent.getLongExtra("groupId", 0)
        }
        showImageAdapter = ShowImageAdapter(showImageList, this@PostActivity)
        with(binding) {
            listOfImages.apply {
                layoutManager =
                    LinearLayoutManager(this@PostActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = showImageAdapter
            }
        }
        registerObserverLoadingEvent(viewModel, this@PostActivity)
    }

    override fun initListener() {
        with(binding) {
            val apiKey = getString(R.string.api_key)
            Places.initialize(this@PostActivity, apiKey)
            imgBack.setOnClickListener {
                finish()
            }
            linearPostImage.setOnClickListener {
                viewModel.postType = "image"
                listOfImages.visibility = View.VISIBLE
                cardViewShowImage.visibility = View.GONE
                imagePickerLauncher.launch(createConfig(false, 5))
            }

            linearPostVideo.setOnClickListener {
                viewModel.postType = "video"
                listOfImages.visibility = View.GONE
                cardViewShowImage.visibility = View.VISIBLE
                chooseVideo()
            }

            linearCheckIn.setOnClickListener {
                startAutoCompleteActivity()
            }
            textSharePost.setOnClickListener {
                when (viewModel.postType) {
                    "image" -> {
                        viewModel.uploadImage(
                            imageUri,
                            imagePath,
                            edtDescription.text.toString(),
                            from,
                            groupId
                        )
                    }
                    "video" -> {
                        viewModel.uploadVideo(
                            videoUri,
                            videoPath,
                            edtDescription.text.toString(),
                            from,
                            groupId
                        )
                    }
                    else -> {
                        viewModel.uploadQuestion(
                            PostNewsFeedRequest(
                                id = "${Calendar.getInstance().time.time}${viewModel.firebaseUser?.uid}",
                                description = edtDescription.text.toString(),
                                imagesList = arrayListOf(),
                                userId = viewModel.firebaseUser?.uid.toString(),
                                checkInTimestamp = Calendar.getInstance().time.time.toString(),
                                checkInAddress = viewModel.checkInAddress,
                                checkInLatitude = viewModel.checkInLatitude,
                                checkInLongitude = viewModel.checkInLongitude,
                                type = "question",
                                videoUrl = null,
                                question = edtDescription.text.toString(),
                            )
                        )
                    }
                }
            }
        }
    }

    override fun observerLiveData() {
        with(viewModel) {
            uploadImageResponse.observe(this@PostActivity) {
                if (it) {
                    showToast(this@PostActivity, resources.getString(R.string.str_success))
                    if (from == "group") {
                        finish()
                    } else {
                        startActivity(Intent(this@PostActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                } else {
                    showToast(this@PostActivity, resources.getString(R.string.error))
                    if (from == "group") {
                        startActivity(Intent(this@PostActivity, GroupActivity::class.java))
                    } else {
                        startActivity(Intent(this@PostActivity, MainActivity::class.java))
                    }
                    finishAffinity()
                }
            }
            uploadVideoResponse.observe(this@PostActivity) {
//                if (checkInLatitude == 0.0 || checkInLongitude == 0.0 || checkInAddress == "") {
//                    viewModel.postNewsFeedRequest = it
//                } else {
                uploadImagePostToDB(it)
                //}
            }
            uploadImageList.observe(this@PostActivity) {
//                if (checkInLatitude == 0.0 || checkInLongitude == 0.0 || checkInAddress == "") {
//                    viewModel.postNewsFeedRequest = it
//                } else {
                uploadImagePostToDB(it)
                //           }
            }

            uploadGroupVideoResponse.observe(this@PostActivity) {
//                if (checkInLatitude == 0.0 || checkInLongitude == 0.0 || checkInAddress == "") {
//                    viewModel.groupPostNewsFeedRequest = it
//                } else {
                uploadGroupPostToDB(it)
                //}
            }
            uploadGroupImagesList.observe(this@PostActivity) {
//                if (checkInLatitude == 0.0 || checkInLongitude == 0.0 || checkInAddress == "") {
//                    viewModel.groupPostNewsFeedRequest = it
//                } else {
                uploadGroupPostToDB(it)
                //}
            }
        }
    }

    private fun handleImagePicker(data: List<Image>) {
        if (data.isEmpty()) {
            showToast(this@PostActivity, "Ban chua chon anh nao")
        } else {
            imageUri.clear()
            imagePath.clear()
            showImageList.clear()
            for (i in data.indices) {
                showImageList.add(data[i].uri)
                showImageAdapter.notifyDataSetChanged()
                imageUri.add(data[i].uri)
                imagePath.add(data[i].uri?.let { getFileExtension(it) }.toString())
            }
        }
    }

    private fun startAutoCompleteActivity() {
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startAutocomplete.launch(intent)
    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO || resultCode == RESULT_OK) {
            if (data != null) {
                val size = data.data?.let { Utils.getFileName(this@PostActivity, it) }
                    ?.let { getFileSize(it) }
                Log.d("CheckSize", size.toString())
                if (size != null) {
                    if (size > 25) {
                        videoUri = data.data
                        videoPath = videoUri?.let { getFileExtension(it) }
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(this@PostActivity, videoUri)
                        binding.imageShowVideo.setImageBitmap(mmr.frameAtTime)
                    } else {
                        showToast(this@PostActivity, "File không được vượt quá 25mb")
                    }
                }
            }
        }
    }

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d("CheckHere", "${result.resultCode} and ${result.data?.extras} and")
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    binding.textLocation.visibility = View.VISIBLE
                    binding.textLocation.text = place.address?.toString() ?: "VietNam"
                    viewModel.checkInAddress = place.address?.toString() ?: "VietNam"
                    viewModel.checkInLatitude = place.latLng?.latitude ?: 21.028511
                    viewModel.checkInLongitude = place.latLng?.longitude ?: 105.804817
                    if (from == "home") {
                        if (viewModel.postNewsFeedRequest != null) {
                            val data = viewModel.postNewsFeedRequest?.let {
                                PostNewsFeedRequest(
                                    it.description,
                                    it.id,
                                    it.imagesList,
                                    it.userId,
                                    it.checkInTimestamp,
                                    place.name?.toString() ?: "Hanoi",
                                    place.latLng?.latitude ?: 21.028511,
                                    place.latLng?.longitude ?: 105.804817,
                                    it.type,
                                    it.videoUrl,
                                    it.question
                                )
                            }
                            data?.let { viewModel.uploadImagePostToDB(it) }
                        }
                    } else {

                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                binding.textLocation.visibility = View.GONE
            }
        }

    override fun clickOnClose(position: Int) {
        showImageList.removeAt(position)
        showImageAdapter.notifyDataSetChanged()
        imageUri.removeAt(position)
        imagePath.removeAt(position)
    }

    private fun getFileSize(fileName: String): Long {
        val file = File(fileName)
        val sizeInBytes: Long = file.length()
        return sizeInBytes / (1024 * 1024)
    }
}