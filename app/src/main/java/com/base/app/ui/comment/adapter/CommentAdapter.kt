package com.base.app.ui.comment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.common.FIREBASE_URL
import com.base.app.data.models.Comment
import com.base.app.databinding.CommentItemBinding
import com.base.app.ui.comment.CommentViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CommentAdapter(
    val list: ArrayList<Comment>,
    val context: Context,
    val viewModel: CommentViewModel,
    private val listener: OnReplyComment
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private lateinit var replyCommentAdapter: ReplyCommentAdapter
    val databaseReference: DatabaseReference = Firebase.database(FIREBASE_URL).reference

    inner class ViewHolder(val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = list[position]
            with(binding) {
                textReplyComment.setOnClickListener {
                    listener.replyComment(data, txtCommentUserName.text.toString())
                }
                viewModel.getReplyCommentsNumber(
                    data.commentid.toString(),
                    textShowReply,
                    viewLine,viewLine1
                )
                textShowReply.setOnClickListener {
                    replyCommentAdapter = ReplyCommentAdapter(viewModel)
                    val replyList: ArrayList<Comment> = ArrayList()
                    textShowReply.isVisible = false
                    listReplyInsideComment.isVisible = true
                    listReplyInsideComment.apply {
                        layoutManager = LinearLayoutManager(root.context)
                        adapter = replyCommentAdapter
                    }
                    databaseReference.child("ReplyComment").child(data.commentid.toString())
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                replyList.clear()
                                for (dataSnap in snapshot.children) {
                                    val comment = dataSnap.getValue(Comment::class.java)
                                    if (comment != null) {
                                        replyList.add(comment)
                                    }
                                }
                                replyCommentAdapter.submitList(replyList.toList())
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
                txtComment.text = data.comment
                data.publisher?.let {
                    viewModel.getInformation(
                        context,
                        imgCommentAvatar,
                        txtCommentUserName,
                        it
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnReplyComment {
        fun replyComment(data: Comment, userName: String)
    }
}