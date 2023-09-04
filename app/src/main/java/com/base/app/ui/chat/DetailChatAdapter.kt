package com.base.app.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.data.models.ChatModel
import com.base.app.databinding.ItemChatBinding
import com.base.app.databinding.ItemImageChatDetailBinding
import com.bumptech.glide.Glide

class DetailChatAdapter(
    private val uid: String,
    private val listener: OnMessageClick
) : ListAdapter<ChatModel, RecyclerView.ViewHolder>(DetailChatDiffUtil) {
    class MessageViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatModel, uid: String) = with(binding) {
            if (data.sender == uid) {
                txtChat1.visibility = View.GONE
                txtChat2.visibility = View.VISIBLE
                txtChat2.text = data.message
            } else {
                txtChat2.visibility = View.GONE
                txtChat1.visibility = View.VISIBLE
                txtChat1.text = data.message
            }
        }
    }

    class ImageViewHolder(private val binding: ItemImageChatDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ChatModel, uid: String, listener: OnMessageClick) = with(binding) {
            if (data.sender != uid) {
                imageMessageLeft.isVisible = true
                imageMessageRight.isVisible = false
                imageMessageLeft.setOnClickListener {
                    listener.onImageClick(data.imageUrl.toString())
                }
                Glide.with(root.context).load(data.imageUrl).placeholder(R.mipmap.ic_launcher)
                    .into(imageMessageLeft)
            } else {
                imageMessageLeft.isVisible = false
                imageMessageRight.isVisible = true
                imageMessageRight.setOnClickListener {
                    listener.onImageClick(data.imageUrl.toString())
                }
                Glide.with(root.context).load(data.imageUrl).placeholder(R.mipmap.ic_launcher)
                    .into(imageMessageRight)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_chat -> {
                MessageViewHolder(
                    ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            R.layout.item_image_chat_detail -> {
                ImageViewHolder(
                    ItemImageChatDetailBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }

    }

    private object DetailChatDiffUtil : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem.receiver == newItem.receiver && oldItem.sender == newItem.sender
                    && oldItem.message == newItem.message && oldItem.timestamp == newItem.timestamp
                    && oldItem.type == newItem.type && oldItem.imageUrl == newItem.imageUrl
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(getItem(position), uid,listener)
            }
            is MessageViewHolder -> {
                holder.bind(getItem(position), uid)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            "message" -> {
                R.layout.item_chat
            }
            "image" -> {
                R.layout.item_image_chat_detail
            }
            else -> {
                throw IllegalArgumentException("Invalid item type")
            }
        }
    }

    interface OnMessageClick {
        fun onImageClick(data: String)
    }
}