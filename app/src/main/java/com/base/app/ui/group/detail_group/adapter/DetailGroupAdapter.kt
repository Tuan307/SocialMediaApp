package com.base.app.ui.group.detail_group.adapter

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.LayoutDetailGroupInformationBinding
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.base.app.ui.group.detail_group.GroupDetailViewModel
import com.base.app.ui.group.detail_group.viewdata.DetailGroupInformationViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupPostViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData
import com.base.app.ui.main.fragment.home.PostInPostAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author tuanpham
 * @since 10/13/2023
 */
class DetailGroupAdapter(
    private val listener: IGroupPostCallBack,
    private val viewModel: GroupDetailViewModel
) :
    ListAdapter<DetailGroupViewData, RecyclerView.ViewHolder>(DetailGroupViewData.DetailGroupDiffUtil) {

    class InformationViewHolder(val binding: LayoutDetailGroupInformationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: DetailGroupInformationViewData,
            listener: IGroupPostCallBack,
            viewModel: GroupDetailViewModel
        ) =
            with(binding) {
                Glide.with(root.context).load(data.imageUrl).into(imageGroupBanner)
                Glide.with(root.context).load(data.userImageUrl).into(imageAvatar)
                textGroupName.text = data.groupName
                if (viewModel.firebaseUser?.uid.toString() == data.groupOwnerId) {
                    buttonJoinGroup.visibility = View.GONE
                    buttonManageGroup.visibility = View.VISIBLE
                    buttonInviteGroup.visibility = View.VISIBLE
                } else {
                    buttonJoinGroup.visibility = View.VISIBLE
                    buttonManageGroup.visibility = View.GONE
                    buttonInviteGroup.visibility = View.VISIBLE
                }
                buttonInviteGroup.setOnClickListener {
                    listener.inviteFriend(data.id)
                }
                buttonManageGroup.setOnClickListener {
                    listener.manageGroup(data.id)
                }
                buttonJoinGroup.setOnClickListener {
                    if (data.hasJoined || buttonJoinGroup.text == "Đã tham gia") {
                        listener.leaveGroup(data.id)
                    } else if (data.hasRequested || buttonJoinGroup.text == "Đã gửi yêu cầu") {
                        listener.removeRequestJoinGroup(data)
                    } else {
                        listener.requestJoinGroup(data)
                    }
                }
                textGroupPrivacy.text = if (data.groupPrivacy == "private") {
                    "Riêng tư"
                } else {
                    "Công khai"
                }
                buttonJoinGroup.text = if (data.hasRequested) {
                    "Đã gửi yêu cầu"
                } else {
                    "Tham gia"
                }
                textMemberNumber.text = data.groupMemberNumber
                if (data.hasJoined) {
                    buttonInviteGroup.visibility = View.VISIBLE
                    if (viewModel.firebaseUser?.uid.toString() == data.groupOwnerId) {
                        buttonJoinGroup.visibility = View.GONE
                    } else {
                        buttonJoinGroup.visibility = View.VISIBLE
                        buttonJoinGroup.text = "Đã tham gia"
                    }
                } else {
                    buttonInviteGroup.visibility = View.GONE
                    buttonJoinGroup.visibility = View.VISIBLE
                    buttonJoinGroup.text = "Tham gia"
                    buttonManageGroup.visibility = View.GONE
                }
                inputThinking.setOnClickListener {
                    listener.onPostAction()
                }
            }
    }

    class GroupPostViewHolder(val binding: LayoutHomeAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mediaPlayer: MediaPlayer? = null
        fun bind(
            data: DetailGroupPostViewData,
            listener: IGroupPostCallBack,
            viewModel: GroupDetailViewModel
        ) = with(binding) {
            if (data.checkInAddress.isEmpty()) {
                textLocation.visibility = View.GONE
            }
            if (data.type == "image") {
                val postInPostAdapter =
                    PostInPostAdapter(
                        root.context,
                        data.imagesList,
                        object : PostInPostAdapter.OnImageCLick {
                            override fun onImageClick(position: Int) {
                                listener.clickToSeeDetail(data.imagesList.orEmpty(), position)
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
            textLocation.text = data.checkInAddress
            textTimeAgo.text = data.createdAt
            if (data.description == "") {
                txtDescription.visibility = View.GONE
            } else {
                txtDescription.visibility = View.VISIBLE
                txtDescription.text = data.description
            }
            data.id.let { viewModel.isLikeGroupPost(it, imgHeart) }
            data.id.let { viewModel.isSavedPost(it, imgSave) }
            txtUserName.text = data.user.userName
            txtPublisher.text = data.user.fullName
            Glide.with(root.context).load(data.user.imageUrl).into(imgAvatar)
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
                    }
                    true
                }
                if (data.user.userId != viewModel.firebaseUser?.uid.toString()) {
                    popupMenu.menu.findItem(R.id.edit).isVisible = false
                    popupMenu.menu.findItem(R.id.delete).isVisible = false
                }
                popupMenu.show()
            }
            imgHeart.setOnClickListener {
                data.id.let { it1 ->
                    data.user.userId.let { it2 ->
                        if (it2 != null) {
                            listener.likePost(
                                it1, imgHeart.tag.toString(),
                                it2
                            )
                        }
                    }
                }
            }
            imgComment.setOnClickListener {
                if (data.id != null && data.user.userId != null) {
                    listener.commentPost(
                        data.id,
                        data.user.userId,
                        data.imagesList?.get(0)?.imageUrl.toString()
                    )
                }
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
                    data.user.userId.let { it2 ->
                        if (it2 != null) {
                            listener.clickPost(
                                it1,
                                it2
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_home_adapter -> {
                GroupPostViewHolder(
                    LayoutHomeAdapterBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            R.layout.layout_detail_group_information -> {
                InformationViewHolder(
                    LayoutDetailGroupInformationBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InformationViewHolder -> {
                holder.bind(
                    getItem(position) as DetailGroupInformationViewData,
                    listener,
                    viewModel
                )
            }
            is GroupPostViewHolder -> {
                holder.bind(getItem(position) as DetailGroupPostViewData, listener, viewModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutRes
    }

    interface IGroupPostCallBack {
        fun onPostAction()
        fun clickPost(postId: String, publisherId: String)
        fun clickToSeeDetail(listData: List<ImagesList>, position: Int)
        fun likePost(postId: String, status: String, publisherId: String)
        fun commentPost(postId: String, publisherId: String, imageUrl: String)
        fun savePost(postId: String)
        fun sharePost(post: Drawable)
        fun doubleClickLikePost(postId: String, status: String, publisherId: String)
        fun editImage(postId: String, view: View)
        fun deleteImage(postId: String)
        fun inviteFriend(groupId: String)
        fun manageGroup(groupId: String)
        fun requestJoinGroup(data: DetailGroupInformationViewData)
        fun removeRequestJoinGroup(data: DetailGroupInformationViewData)
        fun leaveGroup(groupId: String)
    }

}