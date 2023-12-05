package com.base.app.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.LayoutItemChatFollowerBinding
import com.bumptech.glide.Glide

/**
 * @author tuanpham
 * @since 12/5/2023
 */
class ChatFollowerAdapter(val listener: (DatingUser) -> Unit) :
    ListAdapter<DatingUser, ChatFollowerAdapter.ViewHolder>(ChatFollowerDiffUtil) {
    inner class ViewHolder(val binding: LayoutItemChatFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DatingUser) = with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(imageAvatar)
            textUserName.text = data.userName
            root.setOnClickListener {
                listener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemChatFollowerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object ChatFollowerDiffUtil : DiffUtil.ItemCallback<DatingUser>() {
        override fun areItemsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: DatingUser, newItem: DatingUser): Boolean {
            return oldItem == newItem
        }

    }
}