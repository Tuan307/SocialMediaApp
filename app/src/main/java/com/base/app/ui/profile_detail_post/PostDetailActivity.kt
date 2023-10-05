package com.base.app.ui.profile_detail_post

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.EMPTY_STRING
import com.base.app.data.models.NotificationData
import com.base.app.data.models.PushNotification
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.FragmentPostDetailBinding
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.main.fragment.home.DetailHomePostActivity
import com.base.app.ui.profile_detail_post.adapter.ProfilePostAdapterDetail
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PostDetailActivity : BaseActivity<FragmentPostDetailBinding>(),
    ProfilePostAdapterDetail.IPostCallBack {
    private val viewModel by viewModels<PostDetailViewModel>()
    private var list: ArrayList<PostContent> = ArrayList()
    private var imagePosition = 0
    private var txtName = ""
    private lateinit var postAdapter: ProfilePostAdapterDetail
    override fun getContentLayout(): Int {
        return R.layout.fragment_post_detail
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@PostDetailActivity)
        val intent = intent
        val idKey = intent.getStringExtra("idKey")
        imagePosition = intent.getIntExtra("imagePosition", 0)
        binding.rcvPhoto.layoutManager = LinearLayoutManager(this@PostDetailActivity)
        postAdapter = ProfilePostAdapterDetail(
            this@PostDetailActivity,
            this@PostDetailActivity,
            viewModel
        )
        binding.rcvPhoto.adapter = postAdapter
        viewModel.getCurrentUserInformation()
        viewModel.getUserDetailPost(idKey.toString(), 50, 1)
    }

    override fun initListener() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    override fun observerLiveData() {
        with(viewModel) {
            detailUserPostResponse.observe(this@PostDetailActivity) {
                list.clear()
                it.data?.let { it1 -> list.addAll(it1) }
                postAdapter.submitList(list.toList())
                binding.rcvPhoto.scrollToPosition(imagePosition)
            }

            userResponse.observe(this@PostDetailActivity) {
                txtName = it.username.toString()
            }
            tokenResponse.observe(this@PostDetailActivity) {
                val notification = PushNotification(
                    to = it,
                    data = NotificationData(
                        "Thông Báo",
                        "$txtName đã thích ảnh của bạn",
                        EMPTY_STRING
                    )
                )
                viewModel.sendNotification(notification)
            }
        }

    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(this@PostDetailActivity, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String, publisherId: String) {
        viewModel.likePost(postId, status, publisherId)
        if (publisherId != viewModel.firebaseUser?.uid.toString()
            && status == "like"
        ) {
            viewModel.getReceiverToken(publisherId)
        }
    }

    override fun clickToSeeDetail(listData: List<ImagesList>, position: Int) {
        val intent = Intent(this@PostDetailActivity, DetailHomePostActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("postPosition", position)
        bundle.putParcelableArrayList("postList", ArrayList(listData))
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun commentPost(postId: String, publisherId: String) {
        val intent = Intent(this@PostDetailActivity, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun savePost(postId: String, status: String) {
        viewModel.savePost(postId, status)
    }

    override fun sharePost(postId: String) {
        //do later
    }

    override fun doubleClickLikePost(postId: String, status: String, publisherId: String) {
        if (status == "like") {
            viewModel.likePost(postId, status, publisherId)
            if (publisherId != viewModel.firebaseUser?.uid.toString()) {
                viewModel.getReceiverToken(publisherId)
            }
        }
    }

    override fun downloadImage(fileName: String, postId: String) {
        downloadImageByUri(fileName, postId)
    }

    override fun editImage(postId: String, view: View) {
        val alertDialog = AlertDialog.Builder(this@PostDetailActivity)
        alertDialog.setTitle("Edit Post")

        val editText = EditText(this@PostDetailActivity)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        editText.layoutParams = layoutParams
        alertDialog.setView(editText)
        //getText(postid, editText)
        //TODO need to be fixed later
    }

    override fun deleteImage(postId: String) {
        viewModel.deletePost(postId)
    }

    private fun downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                this@PostDetailActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(postImage)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + fileName + ".jpg"
                )
            downloadManager.enqueue(request)
            showToast(
                this,
                resources.getString(R.string.str_success),
            )
        } catch (e: Exception) {
            showToast(
                this,
                resources.getString(R.string.str_error),
            )
        }
    }
}