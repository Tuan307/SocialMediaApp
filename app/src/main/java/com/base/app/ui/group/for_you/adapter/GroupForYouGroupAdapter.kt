package com.base.app.ui.group.for_you.adapter

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.LayoutForYouGroupBodyBinding
import com.base.app.databinding.LayoutForYouGroupHeaderBinding
import com.base.app.ui.group.detail_group.GroupDetailViewModel
import com.base.app.ui.group.for_you.viewdata.GroupForYouPostViewData
import com.base.app.ui.group.for_you.viewdata.GroupForYouViewData
import com.base.app.ui.group.for_you.viewdata.GroupYourViewData
import com.base.app.ui.main.fragment.home.PostInPostAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author tuanpham
 * @since 10/18/2023
 */
class GroupForYouGroupAdapter(
    private val viewModel: GroupDetailViewModel,
    private val listener: GroupForYouInteract
) :
    ListAdapter<GroupForYouViewData, RecyclerView.ViewHolder>(GroupForYouViewData.GroupForYouDiffUtil) {

    private class HeaderViewHolder(private val binding: LayoutForYouGroupHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var headerAdapter: GroupForYouHeaderAdapter
        fun bind(data: GroupYourViewData) = with(binding) {
            headerAdapter = GroupForYouHeaderAdapter(data.groups)
            listOfGroup.apply {
                layoutManager =
                    LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = headerAdapter
            }
        }
    }

    private class BodyViewHolder(private val binding: LayoutForYouGroupBodyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: GroupForYouPostViewData,
            viewModel: GroupDetailViewModel,
            listener: GroupForYouInteract,
        ) = with(binding) {
            var mediaPlayer: MediaPlayer? = null
            if (data.address.isEmpty()) {
                textLocation.visibility = View.GONE
            }
            Glide.with(root.context).load(data.postGroup.groupImageUrl).into(imageGroup)
            if (data.type == "image") {
                val postInPostAdapter =
                    PostInPostAdapter(
                        root.context,
                        data.itemList,
                        object : PostInPostAdapter.OnImageCLick {
                            override fun onImageClick(position: Int) {
                                listener.clickToSeeDetail(data.itemList, position)
                            }
                        })
                videoPost.visibility = View.GONE
                imageVideoThumbnail.visibility = View.GONE
                listImagePost.apply {
                    adapter = postInPostAdapter
                }
            } else {
                imageVideoThumbnail.visibility = View.VISIBLE
                videoPost.visibility = View.VISIBLE
                listImagePost.visibility = View.GONE
                val requestOptions = RequestOptions()
                requestOptions.isMemoryCacheable
                Glide.with(root.context).setDefaultRequestOptions(requestOptions)
                    .load(data.videoUrl)
                    .into(imageVideoThumbnail)
                CoroutineScope(Dispatchers.IO).launch {
                    videoPost.setVideoURI(Uri.parse(data.videoUrl))
                }
                videoPost.setOnPreparedListener {
                    imageReplayVideo.visibility = View.GONE
                    imageVideoThumbnail.visibility = View.GONE
                    it.start()
                    mediaPlayer = it
                }
                videoPost.setOnCompletionListener { imageReplayVideo.visibility = View.VISIBLE }
                imageReplayVideo.setOnClickListener {
                    imageReplayVideo.visibility = View.GONE
                    mediaPlayer?.seekTo(0)
                    mediaPlayer?.start()
                }
            }
            textLocation.text = data.address
            textTimeAgo.text = data.createdAt
            if (data.description == "") {
                txtDescription.visibility = View.GONE
            } else {
                txtDescription.visibility = View.VISIBLE
                txtDescription.text = data.description
            }
            data.id.let { viewModel.isLikeGroupPost(it, imgHeart) }
            data.id.let { viewModel.isSavedPost(it, imgSave) }
            txtUserName.text = data.postUser.userName
            txtPublisher.text = data.postUser.fullName
            Glide.with(root.context).load(data.postUser.imageUrl).into(imgAvatar)
            data.id.let { viewModel.getGroupPostLikes(txtLikeNumber, it) }
            data.id.let { viewModel.countGroupPostComments(txtViewAllComments, it) }
            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(root.context, it)
                popupMenu.inflate(R.menu.post_item)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            data.id.let { it1 -> listener.deleteImage(it1) }
                        }
                        R.id.edit -> {
                            data.id.let { it1 -> listener.editImage(it1, it) }
                        }
                        R.id.download -> {
                            if (data.itemList != null) {
                                data.itemList[0].imageUrl.let { it1 ->
                                    listener.downloadImage(
                                        "ảnh được tải từ social app",
                                        it1.toString()
                                    )
                                }
                            }
                        }
                    }
                    true
                }
                if (data.postUser.userId != viewModel.firebaseUser?.uid.toString()) {
                    popupMenu.menu.findItem(R.id.edit).isVisible = false
                    popupMenu.menu.findItem(R.id.delete).isVisible = false
                }
                popupMenu.show()
            }
            imgHeart.setOnClickListener {
                data.id.let { it1 ->
                    data.postUser.userId.let { it2 ->
                        listener.likePost(
                            it1, imgHeart.tag.toString(),
                            it2
                        )
                    }
                }
            }
            imgComment.setOnClickListener {
                listener.commentPost(
                    data.id,
                    data.postUser.userId,
                    data.itemList[0].imageUrl.toString()
                )
            }
            imgSave.setOnClickListener {
                data.id.let { it1 ->
                    listener.savePost(it1)
                }
                if (imgSave.tag.toString() == "save") {
                    imgSave.tag = "saved"
                    imgSave.setImageResource(R.drawable.ic_bookmark_boder_black)
                } else {
                    imgSave.setImageResource(R.drawable.ic_bookmark_border)
                    imgSave.tag = "save"
                }
            }
            txtViewAllComments.setOnClickListener {
                data.id.let { it1 ->
                    data.postUser.userId.let { it2 ->
                        listener.clickPost(
                            it1,
                            it2
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_for_you_group_header -> {
                HeaderViewHolder(
                    LayoutForYouGroupHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.layout_for_you_group_body -> {
                BodyViewHolder(
                    LayoutForYouGroupBodyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position) as GroupYourViewData)
            }
            is BodyViewHolder -> {
                holder.bind(getItem(position) as GroupForYouPostViewData, viewModel,listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

    interface GroupForYouInteract {
        fun clickPost(postId: String, publisherId: String)
        fun clickToSeeDetail(listData: List<ImagesList>, position: Int)
        fun likePost(postId: String, status: String, publisherId: String)
        fun commentPost(postId: String, publisherId: String, imageUrl: String)
        fun savePost(postId: String)
        fun sharePost(post: Drawable)
        fun doubleClickLikePost(postId: String, status: String, publisherId: String)
        fun downloadImage(fileName: String, postId: String)
        fun editImage(postId: String, view: View)
        fun deleteImage(postId: String)
    }
}