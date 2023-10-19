package com.base.app.ui.group.detail_group

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.ActivityGroupDetailBinding
import com.base.app.ui.add_post.PostActivity
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.group.bottom_sheet_fragment.MangeGroupBottomSheetFragment
import com.base.app.ui.group.detail_group.adapter.DetailGroupAdapter
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData
import com.base.app.ui.group.invite_member.InviteMemberActivity
import com.base.app.ui.main.fragment.home.DetailHomePostActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class GroupDetailActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    DetailGroupAdapter.IGroupPostCallBack {
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var binding: ActivityGroupDetailBinding
    private lateinit var detailAdapter: DetailGroupAdapter
    private val viewModel by viewModels<GroupDetailViewModel>()
    private val list: ArrayList<DetailGroupViewData> = arrayListOf()
    private var groupId = 0.toLong()
    private var groupName = ""
    private var isJoined = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_detail)
        setContentView(binding.root)
        val intent = intent
        detailAdapter = DetailGroupAdapter(this@GroupDetailActivity, viewModel)
        groupId = intent.getLongExtra("groupId", 0)
        groupName = intent.getStringExtra("groupName").toString()
        viewModel.checkIfJoinedGroup(groupId)
        with(binding) {
            swipeDetailGroup.setOnRefreshListener(this@GroupDetailActivity)
            imageBack.setOnClickListener {
                finish()
            }
            imageSearch.setOnClickListener {
                startActivity(
                    Intent(
                        this@GroupDetailActivity,
                        SearchGroupPostActivity::class.java
                    ).putExtra("groupId", groupId)
                )
            }
            listOfGroupPost.apply {
                layoutManager = LinearLayoutManager(this@GroupDetailActivity)
                adapter = detailAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfGroupPost.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getGroupPost(groupId, 10, page)
                    }
                }
            }
            listOfGroupPost.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        checkIfJoinedGroupResponse.observe(this@GroupDetailActivity) {
            isJoined = it.data != null
            viewModel.getGroupInformation(groupId, isJoined)
        }
        listInformationResponse.observe(this@GroupDetailActivity) {
            viewModel.getAllGroupMemberInformation(groupId, it)
        }
        listInformationResponse1.observe(this@GroupDetailActivity) {
            list.clear()
            list.add(it)
            detailAdapter.submitList(list.toList())
            if (isJoined) {
                viewModel.getGroupPost(groupId, 10, 1)
            } else {
                if (viewModel.groupPrivacy != "private") {
                    viewModel.getGroupPost(groupId, 10, 1)
                }
            }
        }
        listGroupPostResponse.observe(this@GroupDetailActivity) {
            list.addAll(it)
            detailAdapter.submitList(list.toList())
        }
    }

    override fun onRefresh() {
        viewModel.getGroupInformation(groupId, isJoined)
        endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeDetailGroup.isRefreshing = false
        }, 1200)
    }

    override fun onPostAction() {
        val intent = Intent(this@GroupDetailActivity, PostActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("groupId", groupId)
        startActivity(intent)
    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(this@GroupDetailActivity, CommentActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun clickToSeeDetail(listData: List<ImagesList>, position: Int) {
        val intent = Intent(this@GroupDetailActivity, DetailHomePostActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("postPosition", position)
        bundle.putParcelableArrayList("postList", java.util.ArrayList(listData))
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String, publisherId: String) {
        viewModel.likeGroupPost(postId, status, publisherId)
        if (publisherId != viewModel.firebaseUser?.uid.toString()
            && status == "like"
        ) {
            viewModel.getReceiverToken(publisherId)
        }
    }

    override fun commentPost(postId: String, publisherId: String, imageUrl: String) {
        val intent = Intent(this@GroupDetailActivity, CommentActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }

    override fun savePost(postId: String) {
    }

    override fun sharePost(post: Drawable) {
    }

    override fun doubleClickLikePost(postId: String, status: String, publisherId: String) {
    }

    override fun downloadImage(fileName: String, postId: String) {
        downloadImageByUri(fileName, postId)
    }

    override fun editImage(postId: String, view: View) {
    }

    override fun deleteImage(postId: String) {
        viewModel.deleteGroupPost(postId)
    }

    override fun inviteFriend(groupId: String) {
        val intent = Intent(this@GroupDetailActivity, InviteMemberActivity::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("groupName", groupName)
        startActivity(intent)
    }

    override fun manageGroup(groupId: String) {
        val fragment = MangeGroupBottomSheetFragment.newInstance()
        val bundle = Bundle()
        bundle.putString("from", "manage_group")
        bundle.putLong("groupId", groupId.toLong())
        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "ActionBottomDialog")
    }

    override fun requestJoinGroup(groupId: String) {
    }

    override fun leaveGroup(groupId: String) {
    }

    private fun downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                this@GroupDetailActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
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
            Toast.makeText(
                this@GroupDetailActivity,
                resources.getString(R.string.str_success),
                Toast.LENGTH_SHORT
            )
        } catch (e: Exception) {
            Toast.makeText(
                this@GroupDetailActivity,
                resources.getString(R.string.str_error),
                Toast.LENGTH_SHORT
            )
        }
    }
}