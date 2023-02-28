package com.base.app.ui.profile_detail_post

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.data.models.PostItem
import com.base.app.databinding.FragmentPostDetailBinding
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.main.fragment.home.HomeViewModel
import com.base.app.ui.main.fragment.home.PostAdapter
import java.io.File

class PostDetailActivity : BaseActivity<FragmentPostDetailBinding>(), PostAdapter.IPostCallBack {

    private val viewModel by viewModels<HomeViewModel>()
    private var list: ArrayList<PostItem> = ArrayList()
    private var imageId = 0
    private lateinit var postAdapter: PostAdapter
    override fun getContentLayout(): Int {
        return R.layout.fragment_post_detail
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@PostDetailActivity)
        val intent = intent
        val idKey = intent.getStringExtra("idKey")
        imageId = intent.getIntExtra("imageId", 0)
        binding.rcvPhoto.layoutManager = LinearLayoutManager(this@PostDetailActivity)
        binding.rcvPhoto.setHasFixedSize(true)
        postAdapter = PostAdapter(this@PostDetailActivity, this, viewModel, list)
        binding.rcvPhoto.adapter = postAdapter
        viewModel.getDataDetail(idKey.toString())

    }

    override fun initListener() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            getListResponseDetail.observe(this@PostDetailActivity) {
                list.clear()
                list.addAll(it)
                postAdapter.notifyDataSetChanged()

                binding.rcvPhoto.scrollToPosition(imageId)
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