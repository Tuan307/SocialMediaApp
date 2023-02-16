package com.base.app.ui.add_video_post

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityAddVideoBinding
import com.base.app.ui.main.MainActivity

class AddVideoActivity : BaseActivity<ActivityAddVideoBinding>() {

    private val viewModel by viewModels<AddVideoViewModel>()
    private var videoUri: Uri? = null
    private var videoPath: String? = null
    private val PICK_VIDEO = 10

    override fun getContentLayout(): Int {
        return R.layout.activity_add_video
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@AddVideoActivity)
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            btnChooseVideo.setOnClickListener {
                chooseVideo()
            }
            btnPost.setOnClickListener {
                viewModel.uploadVideo(videoUri, videoPath)
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            uploadVideoResponse.observe(this@AddVideoActivity) {
                if (it) {
                    showToast(this@AddVideoActivity, resources.getString(R.string.str_success))
                    startActivity(Intent(this@AddVideoActivity, MainActivity::class.java))
                    finishAffinity()
                } else {
                    showToast(this@AddVideoActivity, resources.getString(R.string.str_error))
                    startActivity(Intent(this@AddVideoActivity, MainActivity::class.java))
                    finishAffinity()
                }
            }
        }
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
                videoUri = data.data
                videoPath = videoUri?.let { getFileExtension(it) }
                binding.vdVideo.setVideoURI(videoUri)
                binding.vdVideo.start()
            }
        }
    }


}