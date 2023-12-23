package com.base.app.ui.group.detail_group.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.base.app.ui.group.detail_group.GroupDetailViewModel
import com.base.app.ui.group.detail_group.viewdata.SearchGroupViewData
import com.base.app.ui.main.fragment.home.PostInPostAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


/**
 * @author tuanpham
 * @since 9/20/2023
 */
class GroupFeedAdapter(
    private val viewModel: GroupDetailViewModel,
    private val iPostCallBack: ISearchGroupPostCallBack,
) :
    ListAdapter<SearchGroupViewData, GroupFeedAdapter.ViewHolder>(GroupNewsFeedDiffUtil) {

    class ViewHolder(
        private val binding: LayoutHomeAdapterBinding,
        private val viewModel: GroupDetailViewModel,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        var mediaPlayer: MediaPlayer? = null
        fun bind(data: SearchGroupViewData, listener: ISearchGroupPostCallBack) = with(binding) {
            if (data.checkInAddress.isEmpty()) {
                textLocation.visibility = View.GONE
            }
            if (data.type == "image") {
                val postInPostAdapter =
                    PostInPostAdapter(
                        root.context,
                        data.groupPostContentItemList,
                        object : PostInPostAdapter.OnImageCLick {
                            override fun onImageClick(position: Int) {
                                listener.clickToSeeDetail(
                                    data.groupPostContentItemList.orEmpty(),
                                    position
                                )
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
            data.groupPostId.let { viewModel.isLikeGroupPost(it, imgHeart) }
            data.groupPostId.let { viewModel.isSavedPost(it, imgSave) }
            txtUserName.text = data.groupPostUser.userName
            txtPublisher.text = data.groupPostUser.fullName
            Glide.with(root.context).load(data.groupPostUser.imageUrl).into(imgAvatar)
            data.groupPostId.let { viewModel.getGroupPostLikes(txtLikeNumber, it) }
            data.groupPostId.let { viewModel.countGroupPostComments(txtViewAllComments, it) }
            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(root.context, it)
                popupMenu.inflate(R.menu.post_item)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            data.groupPostId.let { it1 -> listener.deleteImage(it1) }
                        }
                        R.id.edit -> {
                            data.groupPostId.let { it1 -> listener.editImage(it1, it) }
                        }
                    }
                    true
                }
                if (data.groupPostUser.userId != viewModel.firebaseUser?.uid.toString()) {
                    popupMenu.menu.findItem(R.id.edit).isVisible = false
                    popupMenu.menu.findItem(R.id.delete).isVisible = false
                }
                popupMenu.show()
            }
            imgHeart.setOnClickListener {
                data.groupPostId.let { it1 ->
                    data.groupPostUser.userId.let { it2 ->
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
                listener.commentPost(
                    data.groupPostId,
                    data.groupPostUser.userId.toString(),
                    data.groupPostContentItemList[0].imageUrl.toString()
                )
            }
            imgSave.setOnClickListener {
                data.groupPostId.let { it1 ->
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
                data.groupPostId.let { it1 ->
                    data.groupPostUser.userId.let { it2 ->
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

        fun clearVideo() = with(binding) {
            if (videoPost.isPlaying) {
                videoPost.stopPlayback()
                videoPost.suspend()
                videoPost.setVideoURI(null)
                if (mediaPlayer != null) {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutHomeAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), viewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), iPostCallBack)
    }

    private object GroupNewsFeedDiffUtil : ItemCallback<SearchGroupViewData>() {
        override fun areItemsTheSame(
            oldItem: SearchGroupViewData,
            newItem: SearchGroupViewData
        ): Boolean {
            return oldItem.groupPostId == newItem.groupPostId
        }

        override fun areContentsTheSame(
            oldItem: SearchGroupViewData,
            newItem: SearchGroupViewData
        ): Boolean {
            return oldItem.groupPostUser == newItem.groupPostUser
                    && oldItem.createdAt == newItem.createdAt
                    && oldItem.checkInLatitude == newItem.checkInLatitude
                    && oldItem.checkInLongitude == newItem.checkInLongitude
                    && oldItem.checkInAddress == newItem.checkInAddress
                    && oldItem.groupPostContentItemList == newItem.groupPostContentItemList
                    && oldItem.description == newItem.description
        }
    }

    interface ISearchGroupPostCallBack {
        fun clickPost(postId: String, publisherId: String)
        fun clickToSeeDetail(listData: List<ImagesList>, position: Int)
        fun likePost(postId: String, status: String, publisherId: String)
        fun commentPost(postId: String, publisherId: String, imageUrl: String)
        fun savePost(postId: String)
        fun sharePost(post: Drawable)
        fun doubleClickLikePost(postId: String, status: String, publisherId: String)
        fun editImage(postId: String, view: View)
        fun deleteImage(postId: String)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clearVideo()
    }
}