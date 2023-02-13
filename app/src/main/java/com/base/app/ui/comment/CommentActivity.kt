package com.base.app.ui.comment

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.common.EMPTY_STRING
import com.base.app.data.models.Comment
import com.base.app.databinding.ActivityCommentBinding
import com.base.app.ui.comment.adapter.CommentAdapter
import com.bumptech.glide.Glide

class CommentActivity : BaseActivity<ActivityCommentBinding>() {

    private val viewModel by viewModels<CommentViewModel>()
    private var postId = EMPTY_STRING
    private var publisherId = EMPTY_STRING
    private var lists = ArrayList<Comment>()
    private lateinit var commentAdapter: CommentAdapter
    override fun getContentLayout(): Int {
        return R.layout.activity_comment
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel,this@CommentActivity)
        val intent = intent
        postId = intent.getStringExtra("postId").toString()
        publisherId = intent.getStringExtra("publisherId").toString()
        viewModel.getImage()
        viewModel.readComments(postId)
        commentAdapter = CommentAdapter(lists, this@CommentActivity, viewModel)

        binding.apply {
            rcvComment.layoutManager = LinearLayoutManager(this@CommentActivity)
            rcvComment.setHasFixedSize(true)
            rcvComment.adapter = commentAdapter
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
            txtPostComment.setOnClickListener {
                if (edtComment.text.isEmpty()) {
                    showToast(this@CommentActivity, "Please input your comment!")
                } else {
                    viewModel.addComments(postId, edtComment.text.toString())
                    viewModel.addNotifications(postId, edtComment.text.toString(), publisherId)
                }
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            getImageResponse.observe(this@CommentActivity) {
                if (it != null) {
                    Glide.with(this@CommentActivity).load(it).into(binding.imgCommentAvatar)
                }
            }
            getCommentResponse.observe(this@CommentActivity) {
                lists.clear()
                lists.addAll(it)
                commentAdapter.notifyDataSetChanged()
            }
            addCommentResponse.observe(this@CommentActivity) {
                if (it) {
                    binding.edtComment.setText("")
                    hideSoftKeyboard()
                } else {
                    showToast(this@CommentActivity, resources.getString(R.string.error))
                }
            }
        }
    }

}