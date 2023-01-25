package com.base.app.ui.comment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.Comment
import com.base.app.databinding.CommentItemBinding
import com.base.app.ui.comment.CommentViewModel

class CommentAdapter(
    val list: ArrayList<Comment>,
    val context: Context,
    val viewModel: CommentViewModel,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val data = list[position]
            binding.apply {
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
}