package com.base.app.ui.profile_detail_post.adapter

import android.content.Context
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
import com.base.app.ui.main.fragment.home.PostInPostAdapter
import com.base.app.ui.profile_detail_post.PostDetailViewModel

class ProfilePostAdapterDetail(
    val context: Context,
    val iPostCallBack: IPostCallBack,
    val viewModel: PostDetailViewModel,
) : ListAdapter<PostContent, ProfilePostAdapterDetail.ViewHolder>(DetailProfileDiffUtil) {

    inner class ViewHolder(
        val binding: LayoutHomeAdapterBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PostContent, position: Int) {
            with(binding) {
                val postInPostAdapter =
                    PostInPostAdapter(
                        binding.root.context,
                        data.imagesList,
                        object : PostInPostAdapter.OnImageCLick {
                            override fun onImageClick(position: Int) {
                                iPostCallBack.clickToSeeDetail(data.imagesList.orEmpty(), position)
                            }
                        })
                listImagePost.apply {
                    adapter = postInPostAdapter
                }
                textLocation.text = data.checkInAddress
                textTimeAgo.text = data.checkInTimestamp
                if (data.description.equals("")) {
                    txtDescription.visibility = View.GONE
                } else {
                    txtDescription.visibility = View.VISIBLE
                    txtDescription.text = data.description
                }
                data.postId?.let { viewModel.isLikePost(it, imgHeart) }
                data.postId?.let { viewModel.isSavedPost(it, imgSave) }
                viewModel.getPublisherInformation(
                    root.context,
                    imgAvatar,
                    txtUserName,
                    txtPublisher,
                    data.postUserId?.userId
                )
                data.postId?.let { viewModel.getPostLikes(txtLikeNumber, it) }
                //data.postid?.let { viewModel.getPostDisLikes(txtUnLikeNumber, it) }
                data.postId?.let { viewModel.countComments(txtViewAllComments, it) }
                imgMore.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
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
                    if (data.postId != null && data.postUserId?.userId != null) {
                        iPostCallBack.commentPost(data.postId, data.postUserId.userId)
                    }
                }
                imgShare.setOnClickListener {
                    data.postId?.let { it1 -> iPostCallBack.sharePost(it1) }
                }
                imgSave.setOnClickListener {
                    data.postId?.let { it1 -> iPostCallBack.savePost(it1, imgSave.tag.toString()) }
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
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutHomeAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    object DetailProfileDiffUtil : ItemCallback<PostContent>() {
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
            return oldItem == newItem
        }

    }

    interface IPostCallBack {
        fun clickPost(postId: String, publisherId: String)
        fun likePost(postId: String, status: String, publisherId: String)
        fun clickToSeeDetail(listData: List<ImagesList>, position: Int)
        fun commentPost(postId: String, publisherId: String)
        fun savePost(postId: String, status: String)
        fun sharePost(postId: String)
        fun doubleClickLikePost(postId: String, status: String, publisherId: String)
        fun editImage(postId: String, view: View)
        fun deleteImage(postId: String)
    }
}
