package com.base.app.ui.main.fragment.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.common.CommonUtils
import com.base.app.data.models.PostItem
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.bumptech.glide.Glide

class PostAdapter(
    val context: Context,
    val iPostCallBack: IPostCallBack,
    val viewModel: HomeViewModel,
    val dataSet: ArrayList<PostItem>,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {


    inner class ViewHolder(
        val binding: LayoutHomeAdapterBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataSet[position]
//            var picAdapter = PostInPostAdapter(context, data.postimage)
//            binding.apply {
//                imgPost.layoutManager =
//                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//                imgPost.setHasFixedSize(true)
//            }
            Glide.with(context).load(data.postimage).into(binding.imgPost)

            binding.apply {
                if (data.description.equals("")) {
                    txtDescription.visibility = View.GONE
                } else {
                    txtDescription.visibility = View.VISIBLE
                    txtDescription.text = data.description
                }
                data.postid?.let { viewModel.isLikePost(it, imgHeart) }
                data.postid?.let { viewModel.isSavedPost(it, imgSave) }
                viewModel.getPublisherInformation(
                    context,
                    imgAvatar,
                    txtUserName,
                    txtPublisher,
                    data.publicher
                )
                data.postid?.let { viewModel.getPostLikes(txtLikeNumber, it) }
                //data.postid?.let { viewModel.getPostDisLikes(txtUnLikeNumber, it) }
                data.postid?.let { viewModel.countComments(txtViewAllComments, it) }
                imgMore.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
                    popupMenu.inflate(R.menu.post_item)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                data.postid?.let { it1 -> iPostCallBack.deleteImage(it1) }
                            }
                            R.id.edit -> {
                                data.postid?.let { it1 -> iPostCallBack.editImage(it1, it) }
                            }
                            R.id.download -> {
                                data.postimage?.let { it1 ->
                                    iPostCallBack.downloadImage(
                                        "Post from meme app",
                                        it1
                                    )
                                }
                            }
                        }
                        true
                    }
                    if (data.publicher != viewModel.firebaseUser?.uid.toString()) {
                        popupMenu.menu.findItem(R.id.edit).isVisible = false
                        popupMenu.menu.findItem(R.id.delete).isVisible = false
                    }
                    popupMenu.show()
                }
                imgPost.setOnClickListener {
                    if (CommonUtils.isDoubleClick()) {
                        imgDoubleClick.visibility = View.VISIBLE
                        Handler(getMainLooper()).postDelayed({
                            imgDoubleClick.visibility = View.INVISIBLE
                        }, 1000)
                        data.postid?.let { it1 ->
                            data.publicher?.let { it2 ->
                                iPostCallBack.doubleClickLikePost(
                                    it1,
                                    imgHeart.tag.toString(),
                                    it2
                                )
                            }
                        }
                    }
                }
                imgHeart.setOnClickListener {
                    data.postid?.let { it1 ->
                        data.publicher?.let { it2 ->
                            iPostCallBack.likePost(
                                it1, imgHeart.tag.toString(),
                                it2
                            )
                        }
                    }
                }
                imgComment.setOnClickListener {
                    if (data.postid != null && data.publicher != null) {
                        iPostCallBack.commentPost(data.postid, data.publicher)
                    }
                }
                imgShare.setOnClickListener {
                    data.postid?.let { it1 -> iPostCallBack.sharePost(imgPost.drawable) }
                }
                imgSave.setOnClickListener {
                    data.postid?.let { it1 -> iPostCallBack.savePost(it1, imgSave.tag.toString()) }
                }
                txtViewAllComments.setOnClickListener {
                    data.postid?.let { it1 ->
                        data.publicher?.let { it2 ->
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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutHomeAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface IPostCallBack {
        fun clickPost(postId: String, publisherId: String)
        fun likePost(postId: String, status: String, publisherId: String)
        fun commentPost(postId: String, publisherId: String)
        fun savePost(postId: String, status: String)
        fun sharePost(post: Drawable)
        fun doubleClickLikePost(postId: String, status: String, publisherId: String)
        fun downloadImage(fileName: String, postId: String)
        fun editImage(postId: String, view: View)
        fun deleteImage(postId: String)
    }
}