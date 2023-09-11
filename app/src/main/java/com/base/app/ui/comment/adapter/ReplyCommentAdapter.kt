package com.base.app.ui.comment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.Comment
import com.base.app.databinding.ReplyCommentItemBinding
import com.base.app.ui.comment.CommentViewModel

/**
 * @author tuanpham
 * @since 9/10/2023
 */
class ReplyCommentAdapter(
    val viewModel: CommentViewModel
) :
    ListAdapter<Comment, ReplyCommentAdapter.ViewHolder>(ReplyCommentDiffUtil) {

    class ViewHolder(
        private val viewModel: CommentViewModel,
        private val binding: ReplyCommentItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment, position: Int, list: List<Comment>) = with(binding) {
            textComment.text = data.comment
            data.publisher?.let {
                viewModel.getInformation(
                    root.context,
                    imageCommentAvatar,
                    textCommentUserName,
                    it
                )
            }
            if (position == list.size - 1) {
                viewLine.visibility = View.INVISIBLE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            viewModel,
            ReplyCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data = getItem(position), position, currentList)
    }

    private object ReplyCommentDiffUtil : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.commentid == newItem.commentid
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.comment == newItem.comment && oldItem.publisher == newItem.publisher
        }

    }
}