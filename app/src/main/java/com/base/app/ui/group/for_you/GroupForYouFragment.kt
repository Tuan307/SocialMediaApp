package com.base.app.ui.group.for_you

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.FragmentGroupForYouBinding
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.group.detail_group.GroupDetailActivity
import com.base.app.ui.group.detail_group.GroupDetailViewModel
import com.base.app.ui.group.for_you.adapter.GroupForYouGroupAdapter
import com.base.app.ui.group.for_you.viewdata.GroupForYouViewData
import com.base.app.ui.group.for_you.viewdata.GroupItemYourGroupViewData
import com.base.app.ui.group.for_you.viewdata.GroupYourViewData
import com.base.app.ui.group.your_group.GroupAllYourGroupActivity
import com.base.app.ui.main.fragment.home.DetailHomePostActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class GroupForYouFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    GroupForYouGroupAdapter.GroupForYouInteract {
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var binding: FragmentGroupForYouBinding
    private val viewModel by viewModels<GroupForYouViewModel>()
    private val detailViewModel by viewModels<GroupDetailViewModel>()
    private lateinit var forYouAdapter: GroupForYouGroupAdapter
    private val list: ArrayList<GroupForYouViewData> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupForYouBinding.inflate(inflater, container, false)
        forYouAdapter = GroupForYouGroupAdapter(detailViewModel, this@GroupForYouFragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            swipeGroupNewsFeed.setOnRefreshListener(this@GroupForYouFragment)
            listOfForYouGroup.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = forYouAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listOfForYouGroup.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getGroupNewsFeed(10, page)
                    }
                }
            }
            listOfForYouGroup.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        viewModel.getYourGroup()
        observeData()
    }

    private fun observeData() = with(viewModel) {
        groupYourGroupResponse.observe(viewLifecycleOwner) {
            list.clear()
            val size = it.data?.size
            val header = it.data?.let { it1 ->
                GroupYourViewData(
                    id = "header",
                    groups = it1.mapIndexed { index, data ->
                        if (index != size?.minus(1)) {
                            GroupItemYourGroupViewData(
                                groupId = data.id ?: -1,
                                groupImage = data.groupImageUrl.toString(),
                                groupName = data.groupName.toString(),
                                isLast = false
                            )
                        } else {
                            GroupItemYourGroupViewData(
                                groupId = data.id ?: -1,
                                groupImage = data.groupImageUrl.toString(),
                                groupName = data.groupName.toString(),
                                isLast = true
                            )
                        }
                    }
                )
            }
            if (header != null) {
                list.add(header)
            }
            viewModel.getGroupNewsFeed(10, 1)
            forYouAdapter.submitList(list.toList())
        }
        groupGroupNewsFeedResponse.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                list.addAll(it)
                forYouAdapter.submitList(list.toList())
            }
        }
    }

    companion object {
        fun newInstance(): GroupForYouFragment {
            return GroupForYouFragment()
        }
    }

    override fun onRefresh() {
        viewModel.getYourGroup()
        endlessRecyclerViewScrollListener.resetState()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeGroupNewsFeed.isRefreshing = false
        }, 1500)
    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(requireContext(), CommentActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun clickToSeeDetail(listData: List<ImagesList>, position: Int) {
        val intent = Intent(requireContext(), DetailHomePostActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("postPosition", position)
        bundle.putParcelableArrayList("postList", java.util.ArrayList(listData))
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String, publisherId: String) {
        detailViewModel.likeGroupPost(postId, status, publisherId)
        if (publisherId != viewModel.firebaseUser?.uid.toString()
            && status == "like"
        ) {
            detailViewModel.getReceiverToken(publisherId)
        }
    }

    override fun commentPost(postId: String, publisherId: String, imageUrl: String) {
        val intent = Intent(requireContext(), CommentActivity::class.java)
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
        detailViewModel.deleteGroupPost(postId)
    }

    override fun onHeaderClick(isLast: Boolean, data: GroupItemYourGroupViewData) {
        if (isLast) {
            val intent = Intent(requireActivity(), GroupAllYourGroupActivity::class.java)
            intent.putExtra("type", 0)
            startActivity(intent)
        } else {
            val intent = Intent(requireActivity(), GroupDetailActivity::class.java)
            intent.putExtra("groupId", data.groupId)
            intent.putExtra("groupName", data.groupName)
            startActivity(intent)
        }
    }

    private fun downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
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
                requireContext(),
                resources.getString(R.string.str_success),
                Toast.LENGTH_SHORT
            )
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.str_error),
                Toast.LENGTH_SHORT
            )
        }
    }
}