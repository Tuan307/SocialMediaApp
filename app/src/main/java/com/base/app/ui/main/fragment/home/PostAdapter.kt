package com.base.app.ui.main.fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.PostItem
import com.base.app.databinding.LayoutHomeAdapterBinding
import com.bumptech.glide.Glide

class PostAdapter(
    val context: Context,
    val iPostCallBack: IPostCallBack,
    val viewModel: HomeViewModel,
    val dataSet: ArrayList<PostItem>,
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    // private lateinit var viewPagerAdapter: ImageViewPagerAdapter

    inner class ViewHolder(
        val binding: LayoutHomeAdapterBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = dataSet[position]
            Glide.with(context).load(data.postimage).into(binding.imgPost)
            // viewPagerAdapter = ImageViewPagerAdapter(context, list)
            //binding.homePager.adapter = viewPagerAdapter
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

                imgHeart.setOnClickListener {
                    data.postid?.let { it1 -> iPostCallBack.likePost(it1, imgHeart.tag.toString()) }
                }
                imgComment.setOnClickListener {
                    if (data.postid != null && data.publicher != null) {
                        iPostCallBack.commentPost(data.postid, data.publicher)
                    }
                }
                imgShare.setOnClickListener {
                    data.postid?.let { it1 -> iPostCallBack.sharePost(it1) }
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
        fun likePost(postId: String, status: String)
        fun disLikePost(postId: String)
        fun commentPost(postId: String, publisherId: String)
        fun savePost(postId: String, status: String)
        fun sharePost(postId: String)
    }
}