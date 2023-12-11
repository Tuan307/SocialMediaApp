package com.base.app.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.user.User
import com.base.app.databinding.SearchItemBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 12/6/2023
 */
class SearchChatFriendAdapter(private val listener: (User) -> Unit) :
    ListAdapter<User, SearchChatFriendAdapter.ViewHolder>(SearchChatFriendDiffUtil) {
    inner class ViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) = with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(binding.imgAvatar)
            binding.txtName.text = data.fullName
            binding.txtUserName.text = data.userName
            root.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object SearchChatFriendDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}