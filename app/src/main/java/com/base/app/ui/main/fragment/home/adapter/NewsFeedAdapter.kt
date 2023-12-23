package com.base.app.ui.main.fragment.home.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.data.models.response.post.PostContent
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.base.app.ui.main.fragment.home.HomeViewModel
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
class NewsFeedAdapter(
    private val viewModel: HomeViewModel,
    private val iPostCallBack: IPostCallBack,
) :
    ListAdapter<PostContent, NewsFeedAdapter.ViewHolder>(NewsFeedDiffUtil) {

    class ViewHolder(
        private val binding: LayoutHomeAdapterBinding,
        private val viewModel: HomeViewModel,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        var mediaPlayer: MediaPlayer? = null
        fun bind(data: PostContent, iPostCallBack: IPostCallBack) = with(binding) {
            when (data.type) {
                "image" -> {
                    relativeContainer.visibility = View.VISIBLE
                    imgSave.visibility = View.VISIBLE
                    imgShare.visibility = View.VISIBLE
                    val postInPostAdapter =
                        PostInPostAdapter(
                            binding.root.context,
                            data.imagesList,
                            object : PostInPostAdapter.OnImageCLick {
                                override fun onImageClick(position: Int) {
                                    iPostCallBack.clickToSeeDetail(
                                        data.imagesList.orEmpty(),
                                        position
                                    )
                                }
                            })
                    textQuestion.visibility = View.GONE
                    videoPost.visibility = View.GONE
                    imageVideoThumbnail.visibility = View.GONE
                    listImagePost.apply {
                        adapter = postInPostAdapter
                    }
                }
                "video" -> {
                    relativeContainer.visibility = View.VISIBLE
                    imgSave.visibility = View.VISIBLE
                    imgShare.visibility = View.VISIBLE
                    textQuestion.visibility = View.GONE
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
                else -> {
                    imgShare.visibility = View.GONE
                    imgSave.visibility = View.GONE
                    txtDescription.visibility = View.GONE
                    textQuestion.visibility = View.VISIBLE
                    textQuestion.text = data.question
                    relativeContainer.visibility = View.GONE
                }
            }
            textLocation.text = data.checkInAddress
            textTimeAgo.text = data.checkInTimestamp
            if (data.description == "") {
                txtDescription.visibility = View.GONE
            } else {
                txtDescription.visibility = View.VISIBLE
                txtDescription.text = data.description
            }
            data.postId?.let { viewModel.isLikePost(it, imgHeart) }
            data.postId?.let { viewModel.isSavedPost(it, imgSave) }
            txtUserName.text = data.postUserId?.userName
            txtPublisher.text = data.postUserId?.fullName
            Glide.with(root.context).load(data.postUserId?.imageUrl).into(imgAvatar)
            data.postId?.let { viewModel.getPostLikes(txtLikeNumber, it) }
            data.postId?.let { viewModel.countComments(txtViewAllComments, it) }
            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(root.context, it)
                popupMenu.inflate(R.menu.post_item)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            data.postId?.let { it1 -> iPostCallBack.deleteImage(it1) }
                        }
                        R.id.edit -> {
                            data.postId?.let { it1 -> iPostCallBack.editImage(it1, it) }
                        }
                    }
                    true
                }
                if (data.postUserId?.userId != viewModel.firebaseUser?.uid.toString()) {
                    popupMenu.menu.findItem(R.id.edit).isVisible = false
                    popupMenu.menu.findItem(R.id.delete).isVisible = false
                }
                popupMenu.show()
            }
//            imgPost.setOnClickListener {
//                if (CommonUtils.isDoubleClick()) {
//                    imgDoubleClick.visibility = View.VISIBLE
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        imgDoubleClick.visibility = View.INVISIBLE
//                    }, 1000)
//                    data.postid?.let { it1 ->
//                        data.publicher?.let { it2 ->
//                            iPostCallBack.doubleClickLikePost(
//                                it1,
//                                imgHeart.tag.toString(),
//                                it2
//                            )
//                        }
//                    }
//                }
//            }
            imgHeart.setOnClickListener {
                data.postId?.let { it1 ->
                    data.postUserId?.userId?.let { it2 ->
                        iPostCallBack.likePost(
                            it1, imgHeart.tag.toString(),
                            it2
                        )
                    }
                }
            }
            imgComment.setOnClickListener {
                Log.d("CheckKa", "Yeah")
                if (data.postId != null && data.postUserId?.userId != null) {
                    iPostCallBack.commentPost(
                        data.postId,
                        data.postUserId.userId,
                        data.imagesList?.get(0)?.imageUrl.toString()
                    )
                }
            }
//            imgShare.setOnClickListener {
//                val url = data.imagesList[0].imageURL
//                data.id?.let { it1 -> iPostCallBack.sharePost(url.drawable) }
//            }
            imgSave.setOnClickListener {
                data.postId?.let { it1 ->
                    iPostCallBack.savePost(it1)
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
                data.postId?.let { it1 ->
                    data.postUserId?.userId?.let { it2 ->
                        iPostCallBack.clickPost(
                            it1,
                            it2
                        )
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

        fun bindWithBundle(data: PostContent, bundle: Bundle) =
            with(binding) {
                if (bundle.containsKey(TIME_STAMP_CHANGED)) {
                    textLocation.text = data.checkInAddress
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            holder.bindWithBundle(getItem(position), bundle)
        }
    }

    private object NewsFeedDiffUtil : ItemCallback<PostContent>() {
        override fun areItemsTheSame(
            oldItem: PostContent,
            newItem: PostContent
        ): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(
            oldItem: PostContent,
            newItem: PostContent
        ): Boolean {
            return oldItem.postUserId == newItem.postUserId
                    && oldItem.checkInTimestamp == newItem.checkInTimestamp
                    && oldItem.checkInLatitude == newItem.checkInLatitude
                    && oldItem.checkInLongitude == newItem.checkInLongitude
                    && oldItem.checkInAddress == newItem.checkInAddress
                    && oldItem.imagesList == newItem.imagesList
                    && oldItem.description == newItem.description
        }

        override fun getChangePayload(oldItem: PostContent, newItem: PostContent): Any {
            val bundle = Bundle()
            if (oldItem.checkInTimestamp != newItem.checkInTimestamp) {
                bundle.putBoolean(TIME_STAMP_CHANGED, true)
            }
            return bundle
        }
    }

    interface IPostCallBack {
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

    companion object {
        const val TIME_STAMP_CHANGED = "TIME_STAMP_CHANGED"
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.clearVideo()
    }
}