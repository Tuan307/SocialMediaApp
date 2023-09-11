package com.base.app.ui.comment

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.common.EMPTY_STRING
import com.base.app.data.models.Comment
import com.base.app.data.models.NotificationData
import com.base.app.data.models.PushNotification
import com.base.app.databinding.ActivityCommentBinding
import com.base.app.ui.comment.adapter.CommentAdapter
import com.base.app.ui.comment.adapter.ReplyCommentAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentActivity : BaseActivity<ActivityCommentBinding>(), CommentAdapter.OnReplyComment {

    private val viewModel by viewModels<CommentViewModel>()
    private var postId = EMPTY_STRING
    private var publisherId = EMPTY_STRING
    private var lists = ArrayList<Comment>()
    private lateinit var commentAdapter: CommentAdapter
    private var name = EMPTY_STRING
    private var uToken = EMPTY_STRING
    private var commentId = ""
    private var isReplyComment = false
    override fun getContentLayout(): Int {
        return R.layout.activity_comment
    }

    override fun initView() {
        registerObserverLoadingEvent(viewModel, this@CommentActivity)
        val intent = intent
        postId = intent.getStringExtra("postId").toString()
        publisherId = intent.getStringExtra("publisherId").toString()
        viewModel.getImage()
        viewModel.readComments(postId)
        commentAdapter =
            CommentAdapter(lists, this@CommentActivity, viewModel, this@CommentActivity)
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
                    if (!isReplyComment) {
                        viewModel.addComments(postId, edtComment.text.toString())
                        viewModel.addNotifications(postId, edtComment.text.toString(), publisherId)
                    } else {
                        isReplyComment = false
                        viewModel.addReplyComments(commentId, edtComment.text.toString())
                    }
                    val notification = PushNotification(
                        NotificationData(
                            "Message",
                            "$name đã comment vào ảnh của bạn",
                            "Comment"
                        ),
                        uToken
                    )
                    viewModel.sendNotification(notification)
                }
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            getImageResponse.observe(this@CommentActivity) {
                if (it != null) {
                    name = it.username.toString()
                    Glide.with(this@CommentActivity).load(it.imageurl)
                        .into(binding.imgCommentAvatar)
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
            addReplyCommentResponse.observe(this@CommentActivity) {
                if (it) {
                    binding.edtComment.setText("")
                    binding.edtComment.hint = getString(R.string.add_a_comment)
                    hideSoftKeyboard()
                } else {
                    showToast(this@CommentActivity, resources.getString(R.string.error))
                }
            }
            userResponse.observe(this@CommentActivity) {
                viewModel.getReceiverToken(it.id.toString())
            }
            tokenResponse.observe(this@CommentActivity) {
                uToken = ""
            }
        }
    }

    override fun replyComment(data: Comment, userName: String) {
        binding.edtComment.hint = "Đang trả lời $userName"
        commentId = data.commentid.toString()
        isReplyComment = true
    }

}